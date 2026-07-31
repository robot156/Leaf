package io.github.jean.core.datalocal.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import io.github.jean.core.datalocal.database.converter.InstantConverter
import io.github.jean.core.datalocal.database.converter.LongListConverter
import io.github.jean.core.datalocal.database.converter.NoteContentConverter
import io.github.jean.core.datalocal.database.dao.BookDao
import io.github.jean.core.datalocal.database.dao.BookNoteDao
import io.github.jean.core.datalocal.database.dao.BookSearchCacheDao
import io.github.jean.core.datalocal.database.entity.BookEntity
import io.github.jean.core.datalocal.database.entity.BookNoteEntity
import io.github.jean.core.datalocal.database.entity.BookSearchCacheEntity

/**
 * 스키마를 변경할 때는 반드시 아래 순서를 지킨다. 파괴적 마이그레이션을 쓰지 않으므로
 * 마이그레이션을 빼먹으면 앱 실행 시점에 [IllegalStateException] 으로 즉시 실패한다.
 *
 * 1. [version] 을 1 올린다.
 * 2. `autoMigrations` 에 `AutoMigration(from = 이전, to = 새버전)` 을 추가한다.
 * 3. 빌드하면 `schemas/` 에 새 버전 json 이 생성되므로 함께 커밋한다.
 *
 * 컬럼/테이블 추가·삭제는 위 3단계로 끝난다. 컬럼 rename 이나 타입 변경은 Room 이 의도를
 * 추론할 수 없으므로 `AutoMigration(spec = ...)` 으로 [androidx.room.RenameColumn] 등을
 * 명시하거나 수동 [androidx.room.migration.Migration] 을 작성한다.
 *
 * 참고: [BookSearchCacheEntity] 는 재생성 가능한 캐시이므로 마이그레이션에서 비워도 무방하다.
 * 반드시 보존해야 하는 것은 사용자가 직접 작성한 [BookEntity], [BookNoteEntity] 다.
 */
@Database(
    entities = [
        BookEntity::class,
        BookNoteEntity::class,
        BookSearchCacheEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
@TypeConverters(
    InstantConverter::class,
    NoteContentConverter::class,
    LongListConverter::class,
)
@ConstructedBy(LeafDatabaseConstructor::class)
abstract class LeafDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao

    abstract fun bookNoteDao(): BookNoteDao

    abstract fun bookSearchCacheDao(): BookSearchCacheDao

    companion object {
        const val NAME = "leaf.db"
    }
}

/**
 * KMP 에서는 Room 이 리플렉션으로 생성 구현체를 찾을 수 없어, 생성자를 명시적으로 연결해야 한다.
 * `actual` 은 각 타깃의 Room KSP 가 만들어 주므로 여기서는 `expect` 만 선언한다.
 */
@Suppress("NO_ACTUAL_FOR_EXPECT", "EXPECT_ACTUAL_IR_INCOMPATIBILITY")
expect object LeafDatabaseConstructor : RoomDatabaseConstructor<LeafDatabase> {
    override fun initialize(): LeafDatabase
}
