package uz.gita.chatapp.presentation.auth

import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uz.gita.chatapp.data.local.TokenStore
import uz.gita.chatapp.data.remote.ApiResult
import uz.gita.chatapp.data.repository.AuthRepository
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val tokenStore: TokenStore
) : ViewModel() {

    private val _phoneUiState = MutableStateFlow<PhoneUiState>(PhoneUiState.Idle)
    val phoneUiState: StateFlow<PhoneUiState> = _phoneUiState.asStateFlow()

    private val _otpUiState = MutableStateFlow<OtpUiState>(OtpUiState.Idle)
    val otpUiState: StateFlow<OtpUiState> = _otpUiState.asStateFlow()

    fun requestOtp(phone: String) {
        viewModelScope.launch {
            _phoneUiState.value = PhoneUiState.Loading

            when (val result = repository.requestOtp(phone)) {
                is ApiResult.Success -> _phoneUiState.value = PhoneUiState.Success
                is ApiResult.Error -> _phoneUiState.value = PhoneUiState.Error(result.error.message)
            }
        }
    }

    fun verifyOtp(phone: String, code: String) {
        viewModelScope.launch {
            _otpUiState.value = OtpUiState.Loading

            val deviceName = "${Build.MANUFACTURER} ${Build.MODEL}"

            when (val result = repository.verifyOtp(phone, code, deviceName)) {
                is ApiResult.Success -> _otpUiState.value = OtpUiState.Success
                is ApiResult.Error -> _otpUiState.value = OtpUiState.Error(result.error.message)
            }
        }
    }

    private val _sessionState = MutableStateFlow<SessionState>(SessionState.Checking)
    val sessionState: StateFlow<SessionState> = _sessionState.asStateFlow()

    init {
        checkSession()
    }

    private fun checkSession() {
        viewModelScope.launch {
            val loggedIn = tokenStore.hasValidSession()
            _sessionState.value =
                if (loggedIn) SessionState.LoggedIn else SessionState.LoggedOut
        }
    }
}