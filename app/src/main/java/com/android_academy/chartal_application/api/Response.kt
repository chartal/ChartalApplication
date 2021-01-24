package com.android_academy.chartal_application.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Response(
    val results: List<Result>,
)

@Serializable
data class Result(
    @SerialName("id")
    val id: Int
)