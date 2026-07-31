package io.github.jean.core.datalocal.database.entity

import androidx.room.Embedded
import androidx.room.Relation
import io.github.jean.core.common.model.BookNoteWithBook

data class BookNoteWithBookEntity(
    @Embedded val note: BookNoteEntity,
    @Relation(
        parentColumn = "book_id",
        entityColumn = "item_id",
    )
    val book: BookEntity,
)

fun BookNoteWithBookEntity.toDomain(): BookNoteWithBook =
    BookNoteWithBook(
        note = note.toDomain(),
        book = book.toDomain(),
    )
