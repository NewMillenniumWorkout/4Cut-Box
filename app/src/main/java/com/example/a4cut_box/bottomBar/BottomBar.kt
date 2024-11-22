package com.example.a4cut_box.bottomBar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun BottomBar(
    onClickHome: () -> Unit,
    onClickCalendar: () -> Unit,
    onClickCamera: () -> Unit,
    onClickMap: () -> Unit,
    onClickSetting: () -> Unit
) {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.primary,
        actions = {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(onClick = onClickHome) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Home",
                        tint = Color.Black
                    )
                }
                IconButton(onClick = onClickCalendar) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Calendar",
                        tint = Color.Black
                    )
                }
                IconButton(onClick = onClickCamera) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Camera",
                        tint = Color.Black
                    )
                }
                IconButton(onClick = onClickMap) {
                    Icon(
                        imageVector = Icons.Default.Place,
                        contentDescription = "Map",
                        tint = Color.Black
                    )
                }
                IconButton(onClick = onClickSetting) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Setting",
                        tint = Color.Black
                    )
                }
            }
        }
    )
}