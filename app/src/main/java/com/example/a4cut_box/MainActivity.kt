package com.example.a4cut_box

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.a4cut_box.auth.SignInPage
import com.example.a4cut_box.auth.SignUpPage
import com.example.a4cut_box.ui.theme._4CutBoxTheme
import com.google.firebase.FirebaseApp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val context = this@MainActivity

            _4CutBoxTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "SignIn",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("SignIn") {
                            SignInPage(
                                onClickSignUp = { navController.navigate("SignUp") },
                                goToFeatureActivity = {
                                    val intent = Intent(context, FeatureActivity::class.java)
                                    context.startActivity(intent)
                                })
                        }
                        composable("SignUp") {
                            SignUpPage(onClickBack = { navController.navigateUp() })
                        }
                    }
                }
            }
        }
    }
}
