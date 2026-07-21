package io.github.jean.core.datalocal.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.jean.core.common.model.Book
import kotlin.time.Instant

@Entity(tableName = "book")
data class BookEntity(
    @PrimaryKey
    @ColumnInfo(name = "item_id")
    val itemId: Long,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "link")
    val link: String,
    @ColumnInfo(name = "author")
    val author: String,
    @ColumnInfo(name = "cover")
    val cover: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "isbn13")
    val isbn13: String,
    @ColumnInfo(name = "publisher")
    val publisher: String,
    @ColumnInfo(name = "created_at")
    val createdAt: Instant,
)

fun BookEntity.toDomain(): Book =
    Book(
        itemId = itemId,
        title = title,
        link = link,
        author = author,
        cover = cover,
        description = description,
        isbn13 = isbn13,
        publisher = publisher,
        createdAt = createdAt,
    )

fun Book.toEntity(): BookEntity =
    BookEntity(
        itemId = itemId,
        title = title,
        link = link,
        author = author,
        cover = cover,
        description = description,
        isbn13 = isbn13,
        publisher = publisher,
        createdAt = createdAt,
    )
