package com.example.a4cut_box.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SettingPage(
    modifier: Modifier = Modifier,
    goToMainActivity: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text("hello SettingPage")
        Button(onClick = { signOutUser(goToMainActivity) }) {
            Text("Sign Out")
        }
    }
}

fun signOutUser(onSuccess: () -> Unit) {
    val auth = FirebaseAuth.getInstance()
    
    auth.signOut()
    onSuccess()
}