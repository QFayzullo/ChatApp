package uz.gita.chatapp.presentation.auth

sealed interface PhoneUiState {
    data object Idle : PhoneUiState
    data object Loading : PhoneUiState
    data object Success : PhoneUiState
    data class Error(val message: String) : PhoneUiState
}