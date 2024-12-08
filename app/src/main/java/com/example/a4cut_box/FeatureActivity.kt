package com.example.a4cut_box

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.a4cut_box.bottomBar.BottomBar
import com.example.a4cut_box.calendar.CalendarPage
import com.example.a4cut_box.camera.CameraPage
import com.example.a4cut_box.camera.CameraSavePage
import com.example.a4cut_box.home.HomePage
import com.example.a4cut_box.map.MapPage
import com.example.a4cut_box.model.FeatureViewModel
import com.example.a4cut_box.photoDetail.PhotoDetailPage
import com.example.a4cut_box.setting.SettingPage
import com.example.a4cut_box.ui.theme.BoxBlack
import com.example.a4cut_box.ui.theme.BoxWhite
import com.example.a4cut_box.ui.theme._4CutBoxTheme


class FeatureActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val context = this@FeatureActivity


        setContent {
            val featureViewModel = viewModel<FeatureViewModel>()
            featureViewModel.listenForElement()
            val navController = rememberNavController()
            var selectedButton by remember { mutableStateOf("") }

            _4CutBoxTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize(),
                    containerColor = Color.White,
                    floatingActionButtonPosition = FabPosition.Center,
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = {
                                navController.navigateUp()
                                navController.navigate("camera")
                                selectedButton = "camera"
                            },
                            shape = CircleShape,
                            modifier = Modifier
                                .offset(y = 60.dp)
                                .size(66.dp),
                            containerColor = BoxBlack,
                            elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 3.dp)
                        ) {
                            Icon(
                                modifier = Modifier.size(36.dp),
                                painter = painterResource(R.drawable.qr_code),
                                contentDescription = "camera",
                                tint = BoxWhite
                            )
                        }
                    },
                    bottomBar = {
                        BottomBar(
                            onClickHome = {
                                navController.navigateUp()
                                navController.navigate("home")
                                selectedButton = "home"
                            },
                            onClickCalendar = {
                                navController.navigateUp()
                                navController.navigate("calendar")
                                selectedButton = "calendar"
                            },
                            onClickMap = {
                                navController.navigateUp()
                                navController.navigate("map")
                                selectedButton = "map"
                            },
                            onClickSetting = {
                                navController.navigateUp()
                                navController.navigate("setting")
                                selectedButton = "setting"
                            },
                            selectedButton = selectedButton
                        )
                    }
                ) { innerPadding ->
                    Modifier.background(Color.White)
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
                            CameraPage(goToCameraSavePage = { imageUri ->
                                navController.navigate("cameraSave/${Uri.encode(imageUri)}")
                            })
                        }
                        composable(
                            "cameraSave/{imageUri}",
                            arguments = listOf(navArgument("imageUri") {
                                type = NavType.StringType
                            })
                        ) { backStackEntry ->
                            val imageUri =
                                Uri.decode(backStackEntry.arguments?.getString("imageUri") ?: "")
                            CameraSavePage(
                                imageUri = imageUri,
                                onClickBack = { navController.navigateUp() },
                                onClickSave = { navController.navigateUp() },
                                featureViewModel = featureViewModel
                            )
                        }

                        composable("map") {
                            MapPage()
                        }
                        composable("setting") {
                            SettingPage(
                                goToMainActivity = {
                                    val intent = Intent(context, MainActivity::class.java)
                                    context.startActivity(intent)
                                },
                                featureViewModel = featureViewModel
                            )
                        }
                    }
                }
            }
        }
    }
}
