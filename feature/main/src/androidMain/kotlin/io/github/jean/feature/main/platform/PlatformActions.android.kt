package io.github.jean.feature.main.platform

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ShareCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import java.io.File
import java.io.FileOutputStream

@Composable
actual fun rememberPlatformActions(): PlatformActions {
    val context = LocalContext.current
    return remember(context) { AndroidPlatformActions(context) }
}

private class AndroidPlatformActions(
    private val context: Context,
) : PlatformActions {
    override fun openWeb(link: String) {
        if (link.isBlank()) return

        val intent =
            Intent(Intent.ACTION_VIEW, link.toUri())
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        runCatching { context.startActivity(intent) }
    }

    override fun sendContactMail(
        appVersion: String,
        osVersion: String,
        device: String,
    ) {
        val intent =
            Intent(Intent.ACTION_SENDTO).apply {
                data = "mailto:".toUri()
                putExtra(Intent.EXTRA_EMAIL, arrayOf(CONTACT_EMAIL))
                putExtra(Intent.EXTRA_SUBJECT, CONTACT_SUBJECT)
                putExtra(
                    Intent.EXTRA_TEXT,
                    contactMailBody(
                        appVersion = appVersion,
                        osVersion = osVersion,
                        device = device,
                        osName = "Android",
                    ),
                )
            }

        try {
            context.startActivity(intent)
        } catch (_: ActivityNotFoundException) {
            Toast.makeText(context, "메일 앱을 찾을 수 없어요", Toast.LENGTH_SHORT).show()
        }
    }

    override fun shareImage(image: ImageBitmap) {
        try {
            val file = File(image.saveToDisk(context))
            val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)

            ShareCompat
                .IntentBuilder(context)
                .setStream(uri)
                .setType("image/png")
                .startChooser()
        } catch (_: Exception) {
            Toast.makeText(context, "공유할 앱을 찾을 수 없어요", Toast.LENGTH_SHORT).show()
        }
    }
}

private fun ImageBitmap.saveToDisk(context: Context): String {
    val fileName = "shared_image_${System.currentTimeMillis()}.png"
    val cachePath = File(context.cacheDir, "images").also { it.mkdirs() }
    val file = File(cachePath, fileName)
    val outputStream = FileOutputStream(file)

    asAndroidBitmap().compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    outputStream.flush()
    outputStream.close()

    return file.absolutePath
}
