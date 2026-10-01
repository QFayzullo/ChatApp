package uz.gita.chatapp.presentation.auth

sealed interface OtpUiState {
    data object Idle : OtpUiState
    data object Loading : OtpUiState
    data object Success : OtpUiState
    data class Error(val message: String) : OtpUiState
}