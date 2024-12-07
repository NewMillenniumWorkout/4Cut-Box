package com.example.a4cut_box.photoDetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.a4cut_box.R

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoDetailPage(modifier: Modifier = Modifier) {
    Scaffold(
        containerColor = Color.White,
        modifier = Modifier.padding(bottom = 32.dp),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("2024.10.27") },
                navigationIcon = {
                    IconButton(onClick = { /* Handle back press */ }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)

            )
        },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color.White)
//                    .verticalScroll(rememberScrollState())
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 48.dp)
                    ) {
                        // Replace this with your image loading library (e.g., Coil)
                        Image(
                            painter = painterResource(id = R.drawable.test_image), // Placeholder
                            contentDescription = "Photo",
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                item {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 48.dp, vertical = 16.dp)
                    )
                    {
                        Row(
                            modifier = Modifier.fillMaxWidth(), // 가로로 꽉 채우기
                            horizontalArrangement = Arrangement.SpaceBetween, // 좌우로 배치
                            verticalAlignment = Alignment.CenterVertically // 수직 가운데 정렬
                        ) {
                            Text(
                                text = "가을 축제",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            IconButton(onClick = { /* 수정 버튼 클릭 이벤트 */ }) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert, // 수정 버튼 아이콘
                                    contentDescription = "Edit",
                                    tint = Color.Gray
                                )
                            }
                        }
                        Spacer(modifier = Modifier.padding(4.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 8.dp) // 아이템 간 간격
                        ) {
                            Icon(
                                imageVector = Icons.Default.Place, // 지도 핀 아이콘
                                contentDescription = "Location Icon",
                                tint = Color.Gray,
                                modifier = Modifier.size(18.dp) // 아이콘 크기 조정
                            )
                            Spacer(modifier = Modifier.width(8.dp)) // 아이콘과 텍스트 사이 간격
                            Text(
                                text = "동작구 상도동",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person, // 사람 아이콘
                                contentDescription = "People Icon",
                                tint = Color.Gray,
                                modifier = Modifier.size(18.dp) // 아이콘 크기 조정
                            )
                            Spacer(modifier = Modifier.width(8.dp)) // 아이콘과 텍스트 사이 간격
                            Text(
                                text = "김민식, 김여진, 이훈의, 장민석",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    )
}