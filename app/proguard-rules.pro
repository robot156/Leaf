# R8 full-mode 기준. 대부분의 라이브러리(Room, Coil, Metro, Coroutines 등)는
# consumer proguard 룰을 동봉하므로, 여기엔 그것으로 커버되지 않는 것만 둔다.

# ── 메타데이터 보존 ────────────────────────────────────────────────
# kotlinx.serialization은 런타임에 애노테이션/제네릭 시그니처를 참조하므로 유지해야 한다.
-keepattributes *Annotation*, Signature, InnerClasses, EnclosingMethod
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations, AnnotationDefault

# Crashlytics 스택트레이스에서 파일/라인 정보를 남겨 난독화된 리포트도 읽을 수 있게 한다.
-keepattributes SourceFile, LineNumberTable
-renamesourcefileattribute SourceFile

# ── kotlinx.serialization ─────────────────────────────────────────
# 라우트(@Serializable data object)·네트워크 DTO가 R8 full-mode에서 serializer 조회에
# 실패하지 않도록, 컴패니언과 serializer()/INSTANCE 를 보존한다.
-if @kotlinx.serialization.Serializable class **
-keepclassmembers public class <1> {
    static <1>$Companion Companion;
}

-if @kotlinx.serialization.Serializable class ** {
    static **$* *;
}
-keepclassmembers class <2>$<3> {
    kotlinx.serialization.KSerializer serializer(...);
}

-if @kotlinx.serialization.Serializable class ** {
    public static ** INSTANCE;
}
-keepclassmembers class <1> {
    public static <1> INSTANCE;
    kotlinx.serialization.KSerializer serializer(...);
}

# ── Ktor (CIO 엔진 명시 사용) ──────────────────────────────────────
# 엔진을 직접 지정해 ServiceLoader 리플렉션은 불필요. 다만 로깅이 참조하는
# JVM 전용 slf4j는 Android에 없어 R8 경고가 나므로 무시한다.
-dontwarn org.slf4j.**
