package io.github.jean.leaf.ios

import androidx.compose.ui.window.ComposeUIViewController
import io.github.jean.feature.main.LeafApp
import platform.UIKit.UIViewController

/**
 * Swift 에서 부르는 진입점.
 *
 * ```swift
 * LeafViewControllerKt.leafViewController()
 * ```
 */
fun leafViewController(): UIViewController =
    ComposeUIViewController {
        LeafApp(viewModelFactory = requireGraph().metroViewModelFactory)
    }
