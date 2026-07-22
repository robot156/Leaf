package io.github.jean.core.ui.animation

import android.animation.ValueAnimator
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalView
import androidx.core.animation.addListener
import androidx.core.graphics.applyCanvas
import androidx.core.graphics.createBitmap
import kotlin.math.hypot
import kotlin.math.roundToInt

typealias MaskAnimActive = (MaskAnimModel, Float, Float) -> Unit

@Stable
private data class AnimState(
    val clickX: Float = 0f,
    val clickY: Float = 0f,
    val maskRadius: Float = 0f,
)

@Composable
fun MaskBox(
    animationDurationMillis: Long = 650L,
    content: @Composable (MaskAnimActive) -> Unit,
) {
    var maskAnimationState by remember { mutableStateOf(MaskAnimModel.Expend) }
    var viewRect by remember { mutableStateOf<Rect?>(null) }
    val paint by remember { mutableStateOf(Paint(Paint.ANTI_ALIAS_FLAG)) }

    var animState by remember { mutableStateOf(AnimState()) }
    var screenshotBitmap by remember { mutableStateOf<Bitmap?>(null) }
    val animatorHolder =
        remember {
            object {
                var value: ValueAnimator? = null
            }
        }
    DisposableEffect(Unit) {
        onDispose {
            animatorHolder.value?.cancel()
            screenshotBitmap?.recycle()
        }
    }

    val windowRootView = LocalView.current.rootView
    val maskAnimActive: MaskAnimActive = clickEvent@{ animModel, clickX, clickY ->
        val viewRect = viewRect ?: return@clickEvent
        if (viewRect.width.roundToInt() <= 0 || viewRect.height.roundToInt() <= 0) return@clickEvent
        animatorHolder.value?.cancel()
        animState =
            animState.copy(
                clickX = clickX,
                clickY = clickY,
                maskRadius =
                    if (animModel == MaskAnimModel.Expend) {
                        0f
                    } else {
                        hypot(
                            windowRootView.width.toFloat(),
                            windowRootView.height.toFloat(),
                        )
                    },
            )
        maskAnimationState = animModel
        val previousBitmap = screenshotBitmap
        val newBitmap =
            createBitmap(
                viewRect.width.roundToInt(),
                viewRect.height.roundToInt(),
            ).applyCanvas {
                translate(-viewRect.left, -viewRect.top)
                windowRootView.draw(this)
            }
        screenshotBitmap = newBitmap
        previousBitmap?.recycle()
        animatorHolder.value =
            ValueAnimator
                .ofFloat(
                    animState.maskRadius,
                    if (animModel == MaskAnimModel.Expend) {
                        hypot(
                            windowRootView.width.toFloat(),
                            windowRootView.height.toFloat(),
                        )
                    } else {
                        0f
                    },
                ).apply {
                    duration = animationDurationMillis
                    interpolator = AccelerateDecelerateInterpolator()
                    addUpdateListener {
                        animState = animState.copy(maskRadius = it.animatedValue as Float)
                    }
                    addListener(
                        onEnd = {
                            if (screenshotBitmap === newBitmap) {
                                screenshotBitmap = null
                            }
                            newBitmap.recycle()
                        },
                    )
                    start()
                }
    }
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .onGloballyPositioned {
                    viewRect = it.boundsInWindow()
                }.drawWithCache {
                    onDrawWithContent {
                        clipRect {
                            this@onDrawWithContent.drawContent()
                        }
                        screenshotBitmap?.takeIf { !it.isRecycled }?.let { bitmap ->
                            with(drawContext.canvas.nativeCanvas) {
                                val layer = saveLayer(null, null)
                                when (maskAnimationState) {
                                    MaskAnimModel.Expend -> {
                                        drawBitmap(bitmap, 0f, 0f, null)
                                        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
                                        drawCircle(
                                            animState.clickX,
                                            animState.clickY,
                                            animState.maskRadius,
                                            paint,
                                        )
                                    }

                                    MaskAnimModel.Shrink -> {
                                        drawCircle(
                                            animState.clickX,
                                            animState.clickY,
                                            animState.maskRadius,
                                            paint,
                                        )
                                        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
                                        drawBitmap(bitmap, 0f, 0f, paint)
                                    }
                                }
                                paint.xfermode = null
                                restoreToCount(layer)
                            }
                        }
                    }
                },
    ) {
        content(maskAnimActive)
    }
}
