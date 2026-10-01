package uz.gita.chatapp.data.repository

import kotlinx.serialization.json.Json
import uz.gita.chatapp.data.local.TokenStore
import uz.gita.chatapp.data.remote.ApiError
import uz.gita.chatapp.data.remote.ApiResult
import uz.gita.chatapp.data.remote.auth.AuthApi
import uz.gita.chatapp.data.remote.auth.OtpRequestBody
import uz.gita.chatapp.data.remote.auth.OtpVerifyBody
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val api: AuthApi,
    private val tokenStore: TokenStore
) {
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun requestOtp(phone: String): ApiResult<Unit> {
        val response = api.requestOtp(OtpRequestBody(phone))
        return if (response.isSuccessful) {
            ApiResult.Success(Unit)
        } else {
            ApiResult.Error(parseError(response.errorBody()?.string()))
        }
    }

    suspend fun verifyOtp(phone: String, code: String, deviceName: String): ApiResult<Unit> {
        val response = api.verifyOtp(OtpVerifyBody(phone, code, deviceName))

        return if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                tokenStore.saveTokens(
                    accessToken = body.accessToken,
                    refreshToken = body.refreshToken,
                    deviceId = body.deviceId
                )
                ApiResult.Success(Unit)
            } else {
                ApiResult.Error(ApiError("UNKNOWN", "Bo'sh javob keldi", false))
            }
        } else {
            ApiResult.Error(parseError(response.errorBody()?.string()))
        }
    }

    private fun parseError(errorBody: String?): ApiError {
        return errorBody?.let { json.decodeFromString<ApiError>(it) }
            ?: ApiError(code = "UNKNOWN", message = "Noma'lum xato", retryable = false)
    }
}