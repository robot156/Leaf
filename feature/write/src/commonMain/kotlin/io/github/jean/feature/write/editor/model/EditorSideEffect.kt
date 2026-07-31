package io.github.jean.feature.write.editor.model

sealed interface EditorSideEffect {
    data object NavigateToBack : EditorSideEffect

    data object NavigateToSearch : EditorSideEffect

    data class NavigateToDetail(
        val noteId: Long,
    ) : EditorSideEffect
}
