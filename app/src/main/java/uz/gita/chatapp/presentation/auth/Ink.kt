package uz.gita.chatapp.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

private val Ink = Color(0xFF0B2E33)
private val InkSoft = Color(0xFF3F6A70)
private val Field = Color(0xFFEAF5F6)
private val Accent = Color(0xFF4F7C82)
private val Studio = Color(0xFFE6F2F3)

@Composable
fun PhoneScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onCodeSent: (phone: String) -> Unit
) {
    var phone by remember { mutableStateOf("") }
    val uiState by viewModel.phoneUiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState) {
        if (uiState is PhoneUiState.Success) {
            onCodeSent(phone)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Studio)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Relay'ga xush kelibsiz",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Ink
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Davom etish uchun telefon raqamingizni kiriting.",
            fontSize = 14.sp,
            color = InkSoft
        )
        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Telefon raqami") },
            placeholder = { Text("+998900000001") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Field,
                unfocusedContainerColor = Field,
                focusedBorderColor = Accent
            )
        )

        Spacer(Modifier.height(16.dp))

        if (uiState is PhoneUiState.Error) {
            Text(
                text = (uiState as PhoneUiState.Error).message,
                color = Color.Red,
                fontSize = 13.sp
            )
            Spacer(Modifier.height(12.dp))
        }

        Button(
            onClick = { viewModel.requestOtp(phone) },
            enabled = phone.isNotBlank() && uiState !is PhoneUiState.Loading,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Accent)
        ) {
            if (uiState is PhoneUiState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text("Kodni yuborish")
            }
        }
    }
}