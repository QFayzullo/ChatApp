package uz.gita.chatapp.data.repository

import kotlinx.serialization.json.Json
import uz.gita.chatapp.data.remote.ApiError
import uz.gita.chatapp.data.remote.ApiResult
import uz.gita.chatapp.data.remote.auth.AuthApi
import uz.gita.chatapp.data.remote.auth.OtpRequestBody
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val api: AuthApi
) {
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun requestOtp(phone: String): ApiResult<Unit> {
        val response = api.requestOtp(OtpRequestBody(phone))

        return if (response.isSuccessful) {
            ApiResult.Success(Unit)
        } else {
            val errorBody = response.errorBody()?.string()
            val apiError = errorBody?.let { json.decodeFromString<ApiError>(it) }
                ?: ApiError(code = "UNKNOWN", message = "Noma'lum xato", retryable = false)
            ApiResult.Error(apiError)
        }
    }
}