package io.github.jean.core.dataremote.network

import android.util.Log
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import io.github.jean.core.common.env.Environment
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

private const val KTOR_LOG_TAG = "Ktor"

private val prettyJson = Json { prettyPrint = true }

private fun logNetwork(message: String) {
    message.lineSequence().forEach { line ->
        val pretty = line.prettyJsonOrNull()
        if (pretty == null) {
            Log.d(KTOR_LOG_TAG, line)
        } else {
            pretty.lineSequence().forEach { Log.d(KTOR_LOG_TAG, it) }
        }
    }
}

private fun String.prettyJsonOrNull(): String? {
    val trimmed = trim()
    if (!trimmed.startsWith("{") && !trimmed.startsWith("[")) return null
    return runCatching {
        prettyJson.encodeToString(JsonElement.serializer(), prettyJson.parseToJsonElement(trimmed))
    }.getOrNull()
}

@ContributesTo(AppScope::class)
@BindingContainer
object NetworkBindings {
    @Provides
    @SingleIn(AppScope::class)
    fun provideHttpClient(environment: Environment): HttpClient =
        HttpClient(CIO) {
            expectSuccess = true

            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        isLenient = true
                    },
                )
            }

            install(Logging) {
                logger =
                    object : Logger {
                        override fun log(message: String) {
                            logNetwork(message)
                        }
                    }
                level = if (environment.isDebug) LogLevel.BODY else LogLevel.NONE
            }
        }
}
