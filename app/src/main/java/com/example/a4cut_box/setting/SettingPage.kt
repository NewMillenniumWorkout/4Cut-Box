package com.example.a4cut_box.setting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.a4cut_box.model.Element
import com.example.a4cut_box.model.FeatureViewModel
import com.example.a4cut_box.ui.theme.BoxBlack
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SettingPage(
    modifier: Modifier = Modifier,
    goToMainActivity: () -> Unit,
    featureViewModel: FeatureViewModel
) {
//    val list = featureViewModel.elements.collectAsState(initial = emptyList()).value
//
//    LazyColumn(modifier = modifier) {
//        item {
//            Column(modifier = Modifier.fillMaxSize()) {
//                Text("hello SettingPage")
//                Button(onClick = { signOutUser(goToMainActivity) }) {
//                    Text("로그아웃")
//                }
//            }
//        }
//        items(list) { item ->
//            ElementItem(element = item)
//        }
//    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 상단 제목
        Text(
            text = "계정 정보",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(top = 24.dp, bottom = 24.dp)
        )

        // 이메일 정보
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(
                text = "이메일",
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = FirebaseAuth.getInstance().currentUser?.email.toString(),
                fontSize = 16.sp
            )
        }

        // 로그아웃 버튼
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp)
        ) {
            Button(
                onClick = { signOutUser(goToMainActivity) },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BoxBlack, // 버튼 배경색
                    contentColor = Color.White    // 텍스트 색상
                ),
                shape = RoundedCornerShape(8.dp),
            ) {
                Text(
                    text = "로그아웃",
                    fontSize = 16.sp
                )
            }
        }
    }
}

fun signOutUser(onSuccess: () -> Unit) {
    val auth = FirebaseAuth.getInstance()

    auth.signOut()
    onSuccess()
}

@Composable
fun ElementItem(element: Element, modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        // 이미지 로드
        AsyncImage(
            model = element.imageUrl,
            contentDescription = "Element Image",
            modifier = Modifier.weight(1f)
        )
        // 텍스트 정보
        Column(modifier = Modifier.weight(2f)) {
            Text(text = "Memo: ${element.memo}")
            Text(text = "Address: ${element.roadAddress}")
            Text(text = "Created At: ${element.createdAt}")
            Text(text = "Tags: ${element.tags.joinToString(", ")}")
        }
    }
}
