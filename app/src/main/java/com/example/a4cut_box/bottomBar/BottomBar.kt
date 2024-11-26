package com.example.a4cut_box.bottomBar

import android.graphics.drawable.shapes.Shape
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.a4cut_box.R
import com.example.a4cut_box.ui.theme.BoxBlack

@Composable
fun BottomBar(
    onClickHome: () -> Unit,
    onClickCalendar: () -> Unit,
    onClickMap: () -> Unit,
    onClickSetting: () -> Unit
) {
    Box(Modifier.fillMaxWidth()) {
        Image(
            painter = painterResource(id = R.drawable.bottom_box),
            contentDescription = "Bottom AppBar Background",
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .align(Alignment.BottomCenter),
            contentScale = ContentScale.FillBounds
        )
        BottomAppBar(
            modifier = Modifier.background(Color.Transparent),
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.primary,
            actions = {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth()
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
                    }
                    Row {
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
            }
        )
    }
}