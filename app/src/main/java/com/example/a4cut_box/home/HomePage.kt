package com.example.a4cut_box.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.a4cut_box.model.FeatureViewModel
import com.example.a4cut_box.ui.theme.BoxWhite
import kotlin.math.sign

@Composable
fun HomePage(navController: NavController, featureViewModel: FeatureViewModel) {
    val list by featureViewModel.elements.collectAsState()
    val shuffledList = remember(list) { list.shuffled() }
    val baseImageSize = 96f
    val step = baseImageSize.toInt() * 1.2f
    val positions = calculateSpiralPositions(shuffledList.size, step)
    var screenCenter by remember { mutableStateOf(Offset.Zero) }
    var dragOffset by remember { mutableStateOf(Offset.Zero) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BoxWhite)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {
                        val closestPosition = positions.minByOrNull { position ->
                            val absolutePosition = Offset(
                                screenCenter.x + (position - dragOffset).x,
                                screenCenter.y + (position - dragOffset).y
                            )
                            absolutePosition.getDistanceTo(screenCenter)
                        }
                        closestPosition?.let {
                            dragOffset = it
                        }
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        dragOffset -= dragAmount * 0.5f
                    }
                )
            }
            .onGloballyPositioned { layoutCoordinates ->
                val size = layoutCoordinates.size
                screenCenter = Offset(size.width / 2f, size.height / 2f)
            },
        contentAlignment = Alignment.Center
    ) {
        positions.forEachIndexed { index, position ->
            val relativePosition = position - dragOffset
            val absolutePosition = Offset(
                screenCenter.x + relativePosition.x,
                screenCenter.y + relativePosition.y
            )

            val distanceFromCenter = absolutePosition.getDistanceTo(screenCenter) / step
            val scaleFactor = (1.5f - distanceFromCenter * 0.4f).coerceIn(0.5f, 1.5f)
            val imageSize = baseImageSize * scaleFactor
            val zIndexValue = 1f / (distanceFromCenter + 1)

            Surface(
                modifier = Modifier
                    .size(imageSize.dp)
                    .offset(
                        x = if (relativePosition.x != 0f && relativePosition.y == 0f) {
                            relativePosition.x.dp + 24.dp * relativePosition.x.sign
                        } else {
                            relativePosition.x.dp
                        },
                        y = if (relativePosition.x == 0f && relativePosition.y != 0f) {
                            relativePosition.y.dp + 24.dp * relativePosition.y.sign
                        } else {
                            relativePosition.y.dp
                        }
                    )
                    .zIndex(zIndexValue)
                    .clickable {
                        navController.navigate("photoDetail/${shuffledList[index].id}")
                    },
                shape = RoundedCornerShape(((distanceFromCenter * 3) + (imageSize / 3)).dp),
                shadowElevation = if (distanceFromCenter < 0.1f) 32.dp else 0.dp
            ) {
                if (index < shuffledList.size) {
                    AsyncImage(
                        model = shuffledList[index].imageUrl,
                        contentDescription = "Image $index",
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.LightGray),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}

fun calculateSpiralPositions(totalCount: Int, step: Float): List<Offset> {
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

private fun Offset.getDistanceTo(other: Offset): Float {
    return kotlin.math.hypot(this.x - other.x, this.y - other.y)
}
