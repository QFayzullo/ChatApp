package uz.gita.chatapp.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import uz.gita.chatapp.presentation.auth.AuthViewModel
import uz.gita.chatapp.presentation.auth.OtpScreen
import uz.gita.chatapp.presentation.auth.PhoneScreen
import uz.gita.chatapp.presentation.auth.SessionState
import uz.gita.chatapp.presentation.main.MainScreen

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Phone : Screen("phone")
    data object Otp : Screen("otp/{phone}") {
        fun createRoute(phone: String) = "otp/$phone"
    }
    data object Main : Screen("main")
}

@Composable
fun ChatNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            val viewModel: AuthViewModel = hiltViewModel()
            val sessionState by viewModel.sessionState.collectAsStateWithLifecycle()

            when (sessionState) {
                SessionState.Checking -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                SessionState.LoggedIn -> {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
                SessionState.LoggedOut -> {
                    navController.navigate(Screen.Phone.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            }
        }

        composable(Screen.Phone.route) {
            PhoneScreen(
                onCodeSent = { phone ->
                    navController.navigate(Screen.Otp.createRoute(phone))
                }
            )
        }

        composable(
            route = Screen.Otp.route,
            arguments = listOf(navArgument("phone") { type = NavType.StringType })
        ) { backStackEntry ->
            val phone = backStackEntry.arguments?.getString("phone") ?: ""
            OtpScreen(
                phone = phone,
                onVerified = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Phone.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Main.route) {
            MainScreen()
        }
    }
}