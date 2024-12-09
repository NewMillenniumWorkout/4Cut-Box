package com.example.a4cut_box.camera

import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
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
import androidx.compose.runtime.LaunchedEffect
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.Base64

suspend fun downloadImage(context: Context, imageUrl: String): Uri? {
    return withContext(Dispatchers.IO) {
        try {
            val url = URL(imageUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.connect()

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = connection.inputStream
                val file = File(context.cacheDir, "downloaded_image.jpg")
                val outputStream = FileOutputStream(file)

                inputStream.use { input ->
                    outputStream.use { output ->
                        input.copyTo(output)
                    }
                }

                return@withContext Uri.fromFile(file)
            } else {
                Log.e("ImageDownload", "Failed to download image: ${connection.responseCode}")
                null
            }
        } catch (e: Exception) {
            Log.e("ImageDownload", "Error downloading image", e)
            null
        }
    }
}

suspend fun expandUrl(shortUrl: String): String = withContext(Dispatchers.IO) {
    val client = OkHttpClient()
    val request = Request.Builder()
        .url(shortUrl)
        .build()

//    Log.d("QR", "here0")
    try {
        client.newCall(request).execute().use { response ->
            val code = response.code
            Log.d("QR", "grey Response code = $code")
            if (code in 300..399 || code == 200) {
                Log.d("QR", "grey here2")
                Log.d("QR", "grey response: ${response.request.url}")
                return@withContext response.request.url.toString() ?: ""
            }
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return@withContext ""
}

@RequiresApi(Build.VERSION_CODES.O)
fun photoGray(qr: String): String {
    // id= 파라미터 추출
    val idParam = qr.substringAfter("id=", "")
    if (idParam.isEmpty()) {
        Log.d("QR", "photoGray: No id param found in URL: $qr")
        return ""
    }

    return try {
        // Base64 디코딩
        val decodedString = String(Base64.getDecoder().decode(idParam))
        Log.d("QR", "photoGray decodedString=$decodedString")

        // sessionId 추출
        val sessionId = decodedString.split("&").find { it.startsWith("sessionId=") }
            ?.substringAfter("sessionId=")

        if (sessionId.isNullOrEmpty()) {
            Log.d("QR", "photoGray: sessionId not found in decodedString")
            return ""
        }

        val result = "https://pg-qr-resource.aprd.io/$sessionId/image.jpg"
        Log.d("QR", "photoGray result=$result")
        result
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}


fun photoism(uid: String, callback: (String?) -> Unit) {
    val client = OkHttpClient()

    // API URL
    val apiUrl = "https://cmsapi.seobuk.kr/v1/etc/seq/resource?uid=$uid"

    // POST 요청 생성
    val request = Request.Builder()
        .url(apiUrl)
        .post(RequestBody.create(null, ByteArray(0))) // 빈 본문 (필요시 수정 가능)
        .build()

    // 비동기 요청
    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
            Log.d("QR", "1 :(")
            callback(null) // 실패 시 null 반환
        }

        override fun onResponse(call: Call, response: Response) {
            Log.d("QR", "response:::$response")
            if (!response.isSuccessful) {
                Log.d("QR", "2 :(")
                callback(null) // 실패 시 null 반환
                return
            }
            val responseBody = response.body?.string()
            Log.d("QR", "pi body" + responseBody)

            if (responseBody != null) {
                // JSON 파싱 및 URL 추출
                callback(extractPicUrl(responseBody))
            } else {
                callback(null)
            }
        }
    })
}

fun extractPicUrl(apiResponse: String): String? {
    return try {
        // JSON 파싱
        val jsonObject = JSONObject(apiResponse)
        val content = jsonObject.getJSONObject("content")
        val fileInfo = content.getJSONObject("fileInfo")
        val picFile = fileInfo.getJSONObject("picFile")

        // URL 추출
        picFile.getString("path")
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

@RequiresApi(Build.VERSION_CODES.O)
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
    var finalUrl by remember { mutableStateOf<String?>(null) } // photoism 결과를 위한 상태

    Box(modifier = Modifier.fillMaxSize()) {
        CameraScreen { scannedCode -> qrCode = scannedCode }
        Image(
            painter = painterResource(id = R.drawable.qr_square), contentDescription = "qr",
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
                onClick = { launcher.launch("image/*") },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(vertical = 32.dp)
            ) {
                Text("직접 사진 선택")
            }
        } else {
            Log.d("QR", "qr origin: $qrCode")
            LaunchedEffect(qrCode) {
                if (qrCode.startsWith("https://pgshort.aprd.io")) {
                    val expanded = expandUrl(qrCode) // 여기서 이미 photogray-download 형태의 URL 획득
                    // photoGray에는 expanded URL을 전달
                    val grayUrl = photoGray(expanded)
                    if (grayUrl.isNotEmpty()) {
                        val grayUri = downloadImage(context, grayUrl)
                        Log.d("QR", "photoGray finalUri: $grayUri")
                        grayUri?.toString()?.let { goToCameraSavePage(it) }
                    } else {
                        // sessionId를 찾지 못한 경우 처리 로직
                        Log.d("QR", "photoGray failed to get a valid URL")
                    }
                } else if (qrCode.startsWith("https://qr.seobuk.kr")) {
                    // seobuk일 경우
                    val expanded = expandUrl(qrCode)
                    Log.d("QR", "pi exp: $expanded")
                    photoism(expanded.substringAfter("u=")) { result ->
                        finalUrl = result
                    }
                }
            }
        }
    }

    // finalUrl(phothoism 결과)이 변경될 때마다 실행
    LaunchedEffect(finalUrl) {
        finalUrl?.let { url ->
            val context = context
            val uri = downloadImage(context, url)
            Log.d("QR", "pi exp2: $uri")
            uri?.toString()?.let { goToCameraSavePage(it) }
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
