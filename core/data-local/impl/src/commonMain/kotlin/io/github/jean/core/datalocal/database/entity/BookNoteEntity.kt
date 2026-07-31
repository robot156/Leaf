package io.github.jean.core.datalocal.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import io.github.jean.core.common.model.BookNote
import kotlin.time.Instant

@Entity(
    tableName = "book_note",
    foreignKeys = [
        ForeignKey(
            entity = BookEntity::class,
            parentColumns = ["item_id"],
            childColumns = ["book_id"],
            onDelete = ForeignKey.RESTRICT,
        ),
    ],
    indices = [Index(value = ["book_id"])],
)
data class BookNoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo(name = "book_id")
    val bookId: Long,
    @ColumnInfo(name = "content")
    val content: List<NoteContentEntity> = emptyList(),
    @ColumnInfo(name = "created_at")
    val createdAt: Instant,
    @ColumnInfo(name = "updated_at")
    val updatedAt: Instant,
)

fun BookNoteEntity.toDomain(): BookNote =
    BookNote(
        id = id,
        bookId = bookId,
        content = content.map { it.toDomain() },
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

fun BookNote.toEntity(): BookNoteEntity =
    BookNoteEntity(
        id = id,
        bookId = bookId,
        content = content.map { it.toEntity() },
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
