package io.github.jean.feature.main.navigation

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ShareCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import io.github.jean.core.ui.navigation.LocalNavigator
import io.github.jean.core.ui.navigation.rememberNavigator
import io.github.jean.feature.home.navigation.HomeRoute
import io.github.jean.feature.home.navigation.homeEntry
import io.github.jean.feature.imageviewer.navigation.ImageViewerRoute
import io.github.jean.feature.imageviewer.navigation.imageViewerEntry
import io.github.jean.feature.intro.navigation.IntroRoute
import io.github.jean.feature.intro.navigation.introEntry
import io.github.jean.feature.note.detail.navigation.NoteDetailRoute
import io.github.jean.feature.note.detail.navigation.noteDetailEntry
import io.github.jean.feature.setting.navigation.SettingRoute
import io.github.jean.feature.setting.navigation.settingEntry
import io.github.jean.feature.settinglicense.navigation.SettingLicenseDetailRoute
import io.github.jean.feature.settinglicense.navigation.SettingLicensesRoute
import io.github.jean.feature.settinglicense.navigation.settingLicenseDetailEntry
import io.github.jean.feature.settinglicense.navigation.settingLicensesEntry
import io.github.jean.feature.settingtheme.navigation.SettingThemeRoute
import io.github.jean.feature.settingtheme.navigation.settingThemeEntry
import io.github.jean.feature.write.navigation.EditorRoute
import io.github.jean.feature.write.navigation.SearchRoute
import io.github.jean.feature.write.navigation.editorEntry
import io.github.jean.feature.write.navigation.searchEntry
import java.io.File
import java.io.FileOutputStream

@Composable
internal fun MainNavHost(modifier: Modifier = Modifier) {
    val navigator = rememberNavigator(startRoute = IntroRoute)
    val context = LocalContext.current

    CompositionLocalProvider(LocalNavigator provides navigator) {
        NavDisplay(
            backStack = navigator.backStack,
            modifier = modifier,
            onBack = { navigator.back() },
            entryDecorators =
                listOf(
                    rememberSaveableStateHolderNavEntryDecorator(),
                    rememberViewModelStoreNavEntryDecorator(),
                ),
            entryProvider =
                entryProvider {
                    introEntry(
                        navigateToHome = { navigator.navigateAndClear(HomeRoute) },
                    )

                    homeEntry(
                        navigateToSetting = { navigator.navigate(SettingRoute) },
                        navigateToEditor = { navigator.navigate(EditorRoute()) },
                        navigateToDetail = { noteId -> navigator.navigate(NoteDetailRoute(noteId)) },
                    )

                    editorEntry(
                        navigateToBack = { navigator.back() },
                        navigateToSearch = { navigator.navigate(SearchRoute) },
                        navigateToDetail = { noteId -> navigator.replaceTop(NoteDetailRoute(noteId)) },
                    )

                    searchEntry(
                        navigateToBack = { navigator.back() },
                        navigateToExternalWeb = { link -> navigateToExternalWeb(context, link) },
                        navigateToImageViewer = { imageUrl -> navigator.navigate(ImageViewerRoute(imageUrl)) },
                    )

                    noteDetailEntry(
                        navigateToBack = { navigator.back() },
                        navigateToEditor = { noteId -> navigator.navigate(EditorRoute(noteId)) },
                        navigateToExternalWeb = { link -> navigateToExternalWeb(context, link) },
                        navigateToExternalApp = { bitmap -> navigateToExternalApp(context, bitmap) },
                        navigateToImageViewer = { imageUrl -> navigator.navigate(ImageViewerRoute(imageUrl)) },
                    )

                    settingEntry(
                        navigateToBack = { navigator.back() },
                        navigateToThemeSetting = { navigator.navigate(SettingThemeRoute) },
                        navigateToLicenses = { navigator.navigate(SettingLicensesRoute) },
                        navigateToEmail = { appVersion, device, osVersion ->
                            sendContactMail(
                                context = context,
                                appVersion = appVersion,
                                osVersion = osVersion,
                                device = device,
                            )
                        },
                    )

                    settingThemeEntry(
                        navigateToBack = { navigator.back() },
                    )

                    settingLicensesEntry(
                        navigateToBack = { navigator.back() },
                        navigateToLicenseDetail = { navigator.navigate(SettingLicenseDetailRoute(it)) },
                    )

                    settingLicenseDetailEntry(
                        navigateToBack = { navigator.back() },
                    )

                    imageViewerEntry(
                        navigateToBack = { navigator.back() },
                    )
                },
        )
    }
}

private fun navigateToExternalWeb(
    context: Context,
    link: String,
) {
    if (link.isBlank()) return

    val intent =
        Intent(Intent.ACTION_VIEW, link.toUri())
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    runCatching { context.startActivity(intent) }
}

private fun sendContactMail(
    context: Context,
    appVersion: String,
    osVersion: String,
    device: String,
) {
    val intent =
        Intent(Intent.ACTION_SENDTO).apply {
            data = "mailto:".toUri()
            putExtra(Intent.EXTRA_EMAIL, arrayOf("gim162913@gmail.com"))
            putExtra(Intent.EXTRA_SUBJECT, "[Leaf] 문의")
            putExtra(
                Intent.EXTRA_TEXT,
                """
                
                
                ─────────────
                앱 버전: $appVersion
                기기: $device
                OS: Android $osVersion
                """.trimIndent(),
            )
        }

    try {
        context.startActivity(intent)
    } catch (_: ActivityNotFoundException) {
        Toast.makeText(context, "메일 앱을 찾을 수 없어요", Toast.LENGTH_SHORT).show()
    }
}

private fun navigateToExternalApp(
    context: Context,
    bitmap: ImageBitmap,
) {
    try {
        val file = File(bitmap.saveToDisk(context))
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
