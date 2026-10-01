package uz.gita.chatapp.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun MainScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE6F2F3)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Tizimga kirdingiz!\n(Chat ro'yxati keyin qo'shiladi)",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0B2E33)
        )
    }
}