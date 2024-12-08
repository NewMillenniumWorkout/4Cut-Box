package com.example.a4cut_box.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import coil.compose.AsyncImage
import com.example.a4cut_box.model.Element
import com.example.a4cut_box.model.FeatureViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SettingPage(
    modifier: Modifier = Modifier,
    goToMainActivity: () -> Unit,
    featureViewModel: FeatureViewModel
) {
    val list = featureViewModel.elements.collectAsState().value

    LazyColumn(modifier = modifier) {
        item {
            Column(modifier = Modifier.fillMaxSize()) {
                Text("hello SettingPage")
                Button(onClick = { signOutUser(goToMainActivity) }) {
                    Text("Sign Out")
                }
            }
        }
        items(list) { item ->
            ElementItem(element = item)
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
