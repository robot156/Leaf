package io.github.jean.core.common.env

interface Environment {
    val isDebug: Boolean
    val baseApiUrl: String
    val apiKey: String
    val appVersion: String
    val osVersion: String
    val deviceModel: String
}
