package com.example.a4cut_box.camera

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
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
        if (qrCode.isNotEmpty()) {
            Text(
                text = "QR 코드 내용: $qrCode",
            )
        } else {
            CameraScreen { scannedCode -> qrCode = scannedCode }
            Image(
                painter = painterResource(id = R.drawable.qr_square), "qr",
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(250.dp)
            )
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