package com.example.a4cut_box.camera

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.a4cut_box.R

@Composable
fun CameraPage(modifier: Modifier = Modifier) {
    Box(modifier = Modifier.fillMaxSize()) {
        CameraScreen()
        Image(
            painter = painterResource(id = R.drawable.qr_square), "qr",
            modifier = Modifier
                .align(Alignment.Center)
                .size(250.dp)
        )
        ElevatedButton(
            onClick = {},
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(vertical = 32.dp)
        ) {
            Text("직접 사진 선택")
        }
    }
}
