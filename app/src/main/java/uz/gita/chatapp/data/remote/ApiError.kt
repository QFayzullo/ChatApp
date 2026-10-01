package uz.gita.chatapp.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class ApiError(
    val code: String,
    val message: String,
    val retryable: Boolean
)