package com.example.a4cut_box.bottomBar

import android.graphics.drawable.shapes.Shape
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a4cut_box.R
import com.example.a4cut_box.ui.theme.BoxBlack
import com.example.a4cut_box.ui.theme.BoxGray

@Composable
fun BottomBar(
    onClickHome: () -> Unit,
    onClickCalendar: () -> Unit,
    onClickMap: () -> Unit,
    onClickSetting: () -> Unit,
    selectedButton: String
) {
    Box(Modifier.fillMaxWidth()) {

        Image(
            painter = painterResource(id = R.drawable.bottom_box),
            contentDescription = "Bottom AppBar Background",
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .align(Alignment.BottomCenter)
                .alpha(0.3f)
                .blur(
                    radius = 4.dp,
                    edgeTreatment = BlurredEdgeTreatment.Unbounded
                ),
            contentScale = ContentScale.FillBounds,
            colorFilter = ColorFilter.tint(BoxBlack)
        )
        Image(
            painter = painterResource(id = R.drawable.bottom_box),
            contentDescription = "Bottom AppBar Background",
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .align(Alignment.BottomCenter),
            contentScale = ContentScale.FillBounds
        )
        BottomAppBar(
            modifier = Modifier.background(Color.Transparent),
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.primary,
            actions = {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButtonWithLabel(
                        onClick = onClickHome,
                        icon= R.drawable.home,
                        description = "Home",
                        label = "홈",
                        color = if (selectedButton == "home") BoxBlack else BoxGray
                    )
                    IconButtonWithLabel(
                        onClick = onClickCalendar,
                        icon= R.drawable.calendar_days,
                        description = "Calendar",
                        label = "달력",
                        color = if (selectedButton == "calendar") BoxBlack else BoxGray
                    )
                    Spacer(modifier = Modifier.size(56.dp))
                    IconButtonWithLabel(
                        onClick = onClickMap,
                        icon= R.drawable.map,
                        description = "Map",
                        label = "지도",
                        color = if (selectedButton == "map") BoxBlack else BoxGray
                    )
                    IconButtonWithLabel(
                        onClick = onClickSetting,
                        icon= R.drawable.settings_8,
                        description = "Setting",
                        label = "설정",
                        color = if (selectedButton == "setting") BoxBlack else BoxGray
                    )
                }
            }
        )
    }
}

@Composable
fun IconButtonWithLabel(
    onClick: () -> Unit,
    icon: Int,
    description: String,
    label: String,
    color: Color,
){
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = BoxBlack
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                modifier = Modifier.size(28.dp),
                painter = painterResource(icon),
                contentDescription = description,
                tint = color
            )
            Text(
                fontSize = 12.sp,
                color = color,
                text = label
            )
        }
    }
}