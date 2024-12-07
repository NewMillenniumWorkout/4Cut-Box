package com.example.a4cut_box.calendar

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a4cut_box.ui.theme.BoxBlack
import com.example.a4cut_box.ui.theme.BoxGray
import java.time.LocalDate
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarPage(modifier: Modifier = Modifier) {
    fun getCalendarDates(year: Int, month: Int): List<LocalDate?> {
        val yearMonth = YearMonth.of(year, month)
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

    val calendarDates = getCalendarDates(2024, 12)
    println(calendarDates.joinToString { it?.toString() ?: " " })

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
            Text(
                color = BoxBlack,
                fontSize = 32.sp,
                fontWeight = FontWeight(900),
                text = "10월"
            )
            IconButton(
                onClick = {}
            ) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = null,
                    modifier = Modifier.size(36.dp),
                    tint = BoxGray
                )
            }
        }

    }
}