package com.example.a4cut_box.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.a4cut_box.model.FeatureViewModel

@Composable
fun HomePage(navController: NavController, featureViewModel: FeatureViewModel) {
    val list by featureViewModel.elements.collectAsState()
    val imageSize = 96
    val positions = calculateSpiralPositions(list.size, imageSize)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        positions.forEachIndexed { index, position ->
            if (index < list.size) {
                AsyncImage(
                    model = list[index].imageUrl,
                    contentDescription = "Image $index",
                    modifier = Modifier
                        .offset(position.x.dp, position.y.dp)
                        .clip(RoundedCornerShape((32.dp)))
                        .size(imageSize.dp)
                        .padding(4.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

fun calculateSpiralPositions(totalCount: Int, step: Int): List<Offset> {
    val positions = mutableListOf<Offset>()

    val directions = listOf(
        Offset(1f, 0f),
        Offset(0f, 1f),
        Offset(-1f, 0f),
        Offset(0f, -1f)
    )

    var currentDirection = 0
    var x = 0
    var y = 0
    var steps = 1

    positions.add(Offset(0f, 0f))
    for (i in 1 until totalCount) {
        x += (directions[currentDirection].x * step).toInt()
        y += (directions[currentDirection].y * step).toInt()
        positions.add(Offset(x.toFloat(), y.toFloat()))

        if (i % steps == 0) {
            currentDirection = (currentDirection + 1) % 4
            if (currentDirection == 0 || currentDirection == 2) {
                steps++
            }
        }
    }

    return positions
}