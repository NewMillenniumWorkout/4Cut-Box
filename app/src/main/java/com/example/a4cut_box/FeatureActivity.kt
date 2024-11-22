package com.example.a4cut_box

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.a4cut_box.bottomBar.BottomBar
import com.example.a4cut_box.calendar.CalendarPage
import com.example.a4cut_box.camera.CameraPage
import com.example.a4cut_box.camera.CameraSavePage
import com.example.a4cut_box.home.HomePage
import com.example.a4cut_box.map.MapPage
import com.example.a4cut_box.photoDetail.PhotoDetailPage
import com.example.a4cut_box.setting.SettingPage
import com.example.a4cut_box.ui.theme._4CutBoxTheme


class FeatureActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val context = this@FeatureActivity

        setContent {
            val navController = rememberNavController()

            _4CutBoxTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomBar(
                            onClickHome = {
                                navController.navigateUp()
                                navController.navigate("home")
                            },
                            onClickCalendar = {
                                navController.navigateUp()
                                navController.navigate("calendar")
                            },
                            onClickCamera = {
                                navController.navigateUp()
                                navController.navigate("camera")
                            },
                            onClickMap = {
                                navController.navigateUp()
                                navController.navigate("map")
                            },
                            onClickSetting = {
                                navController.navigateUp()
                                navController.navigate("setting")
                            },
                        )
                    }
                ) { innerPadding ->
                    NavHost(
                        modifier = Modifier.padding(innerPadding),
                        navController = navController,
                        startDestination = "Home"
                    ) {
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
                            SettingPage(
                                goToMainActivity = {
                                    val intent = Intent(context, MainActivity::class.java)
                                    context.startActivity(intent)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
