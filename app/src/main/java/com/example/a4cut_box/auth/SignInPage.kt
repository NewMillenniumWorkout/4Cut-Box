package com.example.a4cut_box.auth

import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SignInPage(modifier: Modifier = Modifier, onClickSignUp: () -> Unit) {
    Text("hello SignInPage")
    ElevatedButton(onClick = onClickSignUp) {
        Text("Sign Up")
    }
}