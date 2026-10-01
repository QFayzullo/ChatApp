package uz.gita.chatapp.data.remote.auth

import kotlinx.serialization.Serializable

@Serializable
data class OtpVerifyBody(
    val phone: String,
    val code: String,
    val deviceName: String
)

@Serializable
data class AuthTokenResponse(
    val accessToken: String,
    val refreshToken: String,
    val userId: String,
    val deviceId: String,
    val isNewUser: Boolean
)