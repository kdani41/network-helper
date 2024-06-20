package com.kdani.ktor

data class RequestOptions(
    val headers: Map<String, String> = emptyMap(),
    val queryParams: Map<String, String> = emptyMap(),
    val body: Any? = null,
)
