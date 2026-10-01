package uz.gita.chatapp.presentation.auth

sealed interface SessionState {
    data object Checking : SessionState
    data object LoggedIn : SessionState
    data object LoggedOut : SessionState
}