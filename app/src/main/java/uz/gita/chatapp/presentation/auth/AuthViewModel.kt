package uz.gita.chatapp.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uz.gita.chatapp.data.remote.ApiResult
import uz.gita.chatapp.data.repository.AuthRepository
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _phoneUiState = MutableStateFlow<PhoneUiState>(PhoneUiState.Idle)
    val phoneUiState: StateFlow<PhoneUiState> = _phoneUiState.asStateFlow()

    fun requestOtp(phone: String) {
        viewModelScope.launch {
            _phoneUiState.value = PhoneUiState.Loading

            when (val result = repository.requestOtp(phone)) {
                is ApiResult.Success -> {
                    _phoneUiState.value = PhoneUiState.Success
                }
                is ApiResult.Error -> {
                    _phoneUiState.value = PhoneUiState.Error(result.error.message)
                }
            }
        }
    }
}