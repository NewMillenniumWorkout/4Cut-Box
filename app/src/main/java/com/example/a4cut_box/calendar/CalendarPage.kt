package com.example.a4cut_box.calendar

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.a4cut_box.model.Element
import com.example.a4cut_box.model.FeatureViewModel
import com.example.a4cut_box.ui.theme.BoxBlack
import com.example.a4cut_box.ui.theme.BoxGray
import com.example.a4cut_box.ui.theme.BoxWhite
import java.time.LocalDate
import java.time.YearMonth

@SuppressLint("RememberReturnType")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarPage(navController: NavController, featureViewModel: FeatureViewModel) {
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    val list by featureViewModel.elements.collectAsState()
    var selectedDate by remember { mutableStateOf<LocalDate?>(LocalDate.now()) }
    var isDialogOpen by remember { mutableStateOf(false) }
    var dialogImages by remember { mutableStateOf(listOf<Element>()) }

    fun getCalendarDates(yearMonth: YearMonth): List<LocalDate?> {
        val daysInMonth = yearMonth.lengthOfMonth()
        val firstDayOfMonth = yearMonth.atDay(1)
        val startDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7

        val dates = mutableListOf<LocalDate?>()

        for (i in 0 until startDayOfWeek) {
            dates.add(null)
        }

        for (day in 1..daysInMonth) {
            dates.add(yearMonth.atDay(day))
        }

        return dates
    }

    val calendarDates = getCalendarDates(currentMonth)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { currentMonth = currentMonth.minusMonths(1) }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "이전 달",
                    tint = BoxGray
                )
            }
            Text(
                color = BoxBlack,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                text = "${currentMonth.year}년 ${currentMonth.month.value}월"
            )
            IconButton(onClick = { currentMonth = currentMonth.plusMonths(1) }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "다음 달",
                    tint = BoxGray
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            listOf("일", "월", "화", "수", "목", "금", "토").forEachIndexed { index, day ->
                Text(
                    text = day,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = when (index) {
                        0 -> Color(0xFFF57171)
                        6 -> Color(0xFF337FF1)
                        else -> Color.Gray
                    }

                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(calendarDates) { date ->
                val filteredData = list.filter { element ->
                    element.createdAt.let {
                        val elementDate = LocalDate.ofEpochDay(it / (24 * 60 * 60 * 1000))
                        elementDate == date
                    }
                }


                Box(
                    contentAlignment = Alignment.TopCenter,
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(width = 48.dp, height = 104.dp)
                        .padding(2.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            if (filteredData.isNotEmpty()) {
                                selectedDate = date
                                dialogImages = filteredData
                                if (filteredData.size > 1) {
                                    isDialogOpen = true
                                } else {
                                    navController.navigate("photoDetail/${filteredData.first().id}")
                                }
                            }
                        },
                ) {
                    if (date != null) {
                        val textColor = when {
                            selectedDate == date -> BoxWhite
                            date.dayOfWeek.value == 6 -> Color(0xFF337FF1)
                            date.dayOfWeek.value == 7 -> Color(0xFFF57171)
                            else -> BoxBlack
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(if (selectedDate == date) BoxBlack else Color.Transparent)
                            ) {
                                Text(
                                    text = date.dayOfMonth.toString(),
                                    fontSize = 14.sp,
                                    color = textColor,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            if (filteredData.isNotEmpty()) {
                                AsyncImage(
                                    model = filteredData.first().imageUrl,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(1f)
                                        .clip(RoundedCornerShape(20.dp))
                                )
                                if (filteredData.size > 1) {
                                    Text(
                                        text = "+${filteredData.size - 1}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Light,
                                        color = BoxBlack,
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    if (isDialogOpen) {
        Dialog(onDismissRequest = { isDialogOpen = false }) {
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .clip(RoundedCornerShape(64.dp))
                    .background(BoxBlack)
                    .padding(16.dp)
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .heightIn(min = 80.dp, max = 400.dp)
                ) {
                    items(dialogImages) { element ->
                        Box(
                            modifier = Modifier
                                .padding(8.dp)
                                .clip(RoundedCornerShape(48.dp))
                                .border(2.dp, Color.White, RoundedCornerShape(48.dp))
                                .clickable { navController.navigate("photoDetail/${element.id}") }
                        ) {
                            AsyncImage(
                                model = element.imageUrl,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1f)
                            )
                        }
                    }
                }
            }
        }
    }

}
