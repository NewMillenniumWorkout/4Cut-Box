package com.example.a4cut_box

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.a4cut_box.auth.SignInPage
import com.example.a4cut_box.auth.SignUpPage
import com.example.a4cut_box.bottomBar.BottomBar
import com.example.a4cut_box.calendar.CalendarPage
import com.example.a4cut_box.camera.CameraPage
import com.example.a4cut_box.camera.CameraSavePage
import com.example.a4cut_box.home.HomePage
import com.example.a4cut_box.map.MapPage
import com.example.a4cut_box.photoDetail.PhotoDetailPage
import com.example.a4cut_box.setting.SettingPage
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
                    NavHostBody(
                        innerPadding = innerPadding,
                        navController = navController
                    )
                }
            }
        }
    }
}

@Composable
fun NavHostBody(innerPadding: PaddingValues, navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "SignIn",
        modifier = Modifier.padding(innerPadding)
    ) {
        composable("SignIn") {
            SignInPage(onClickSignUp = { navController.navigate("SignUp") })
        }
        composable("SignUp") {
            SignUpPage(onClickBack = { navController.navigateUp() })
        }
        composable("Home") {
            HomePage()
        }
        composable("calendar") {
            CalendarPage()
        }
        composable("photoDetail") {
            PhotoDetailPage()
        }
        composable("camera") {
            CameraPage()
        }
        composable("cameraSave") {
            CameraSavePage()
        }
        composable("map") {
            MapPage()
        }
        composable("setting") {
            SettingPage()
        }
        composable("map") {
            MapPage()
        }
    }
}