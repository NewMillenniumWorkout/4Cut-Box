package com.example.a4cut_box

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
import com.example.a4cut_box.auth.LogInPage
import com.example.a4cut_box.bottomBar.BottomBar
import com.example.a4cut_box.home.HomePage
import com.example.a4cut_box.ui.theme._4CutBoxTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            _4CutBoxTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomBar()
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "LogIn",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("LogIn") {
                            LogInPage()
                        }
                        composable("Home") {
                            HomePage()
                        }
                    }
                }
            }
        }
    }
}
