package com.example.a4cut_box.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SignInPage(modifier: Modifier = Modifier, onClickSignUp: () -> Unit, onClickTmp: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text("hello SignInPage")
        ElevatedButton(onClick = onClickSignUp) {
            Text("Sign Up")
        }
        ElevatedButton(onClick = onClickTmp) {
            Text("Sign In")
        }
    }
}