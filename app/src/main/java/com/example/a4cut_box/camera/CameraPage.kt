package com.example.a4cut_box.camera

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a4cut_box.R

@Composable
fun CameraPage(modifier: Modifier = Modifier, goToCameraSavePage: (image: String) -> Unit) {
    val context = LocalContext.current
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                Log.d("me", "uri: $uri")
                goToCameraSavePage(uri.toString())
            } else {
                Toast.makeText(context, "이미지를 선택하지 않았습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    var qrCode by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        CameraScreen { scannedCode -> qrCode = scannedCode }
        Image(
            painter = painterResource(id = R.drawable.qr_square), "qr",
            modifier = Modifier
                .align(Alignment.Center)
                .size(250.dp)
        )

        val boxColor = if (qrCode.isNotEmpty()) Color(0xFF34C759) else Color(0xFFFF9500)
        val boxText = if (qrCode.isNotEmpty()) "찾았습니다!" else "QR 코드 찾는 중"
        val boxIcon = if (qrCode.isEmpty()) R.drawable.qr_code_small else null

        StatusBox(
            text = boxText,
            iconRes = boxIcon,
            backgroundColor = boxColor,
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = 165.dp)
        )

        if (qrCode.isEmpty()) {
            ElevatedButton(
                onClick = {
                    launcher.launch("image/*")
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(vertical = 32.dp)
            ) {
                Text("직접 사진 선택")
            }
        }
    }
}

@Composable
fun StatusBox(
    text: String,
    iconRes: Int? = null,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(backgroundColor)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(vertical = 4.dp, horizontal = 28.dp)
        ) {
            iconRes?.let {
                Image(
                    painter = painterResource(it),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
            }
            Text(
                text = text,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W700,
                    color = Color.White
                ),
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
    }
}
