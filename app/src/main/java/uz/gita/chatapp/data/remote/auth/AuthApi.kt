package uz.gita.chatapp.data.remote.auth

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("v1/auth/otp/request")
    suspend fun requestOtp(@Body request: OtpRequestBody): Response<Unit>
}