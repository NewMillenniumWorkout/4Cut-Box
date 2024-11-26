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
import androidx.compose.ui.res.painterResource
import com.example.a4cut_box.R
import com.example.a4cut_box.ui.theme.BoxBlack

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
                        painter = painterResource(R.drawable.home),
                        contentDescription = "Home",
                        tint = BoxBlack
                    )
                }
                IconButton(onClick = onClickCalendar) {
                    Icon(
                        painter = painterResource(R.drawable.calendar_days),
                        contentDescription = "Calendar",
                        tint = BoxBlack
                    )
                }
                IconButton(onClick = onClickCamera) {
                    Icon(
                        painter = painterResource(R.drawable.qr_code),
                        contentDescription = "Camera",
                        tint = BoxBlack
                    )
                }
                IconButton(onClick = onClickMap) {
                    Icon(
                        painter = painterResource(R.drawable.map),
                        contentDescription = "Map",
                        tint = BoxBlack
                    )
                }
                IconButton(onClick = onClickSetting) {
                    Icon(
                        painter = painterResource(R.drawable.settings_8),
                        contentDescription = "Setting",
                        tint = BoxBlack
                    )
                }
            }
        }
    )
}