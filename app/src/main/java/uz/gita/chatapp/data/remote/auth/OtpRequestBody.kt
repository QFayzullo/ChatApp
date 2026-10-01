package uz.gita.chatapp.data.remote.auth

import kotlinx.serialization.Serializable

@Serializable
data class OtpRequestBody(
    val phone: String
)