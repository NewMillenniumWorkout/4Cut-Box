package com.example.a4cut_box.photoDetail

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.a4cut_box.R
import com.example.a4cut_box.model.Element
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoDetailPage(
    navController: NavController,
    element: Element,
    modifier: Modifier = Modifier,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Scaffold(
        containerColor = Color.White,
        modifier = Modifier.padding(bottom = 32.dp),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = formatTimestamp(element.createdAt),
                        fontWeight = FontWeight(700)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.left_arrow),
                            contentDescription = "Back",
                            modifier = Modifier.size(32.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)

            )
        },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color.White)
            ) {
                item {
                    Box(
                        modifier = Modifier
//                            .wrapContentSize(Alignment.TopStart)
                            .fillMaxWidth()
                            .padding(start = 48.dp, end = 48.dp, top = 8.dp)
                    ) {
                        AsyncImage(
                            model = element.imageUrl, // Element의 이미지 URL
                            contentDescription = "Photo",
                            modifier = Modifier.fillMaxWidth(),
                            contentScale = ContentScale.FillWidth
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
                            modifier = Modifier
                                .fillMaxWidth(), // 가로로 꽉 채우기
                            horizontalArrangement = Arrangement.SpaceBetween, // 좌우로 배치
                            verticalAlignment = Alignment.CenterVertically // 수직 가운데 정렬
                        ) {
                            Text(
                                text = element.memo,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            Box {
                                IconButton(onClick = { expanded = !expanded }) {
                                    Icon(
                                        imageVector = Icons.Default.MoreVert, // 수정 버튼 아이콘
                                        contentDescription = "Edit",
                                        tint = Color.Gray
                                    )
                                }
                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false }, // 메뉴 외부 클릭 시 닫기
                                    modifier = Modifier
                                        .background(Color.White),
                                    offset = DpOffset(x = 0.dp, y = 0.dp)
                                ) {
                                    DropdownMenuItem(
                                        text = {
                                            Text("수정하기", color = Color.Black)
                                        },
                                        onClick = {
                                            expanded = false
                                            onEditClick() // 수정하기 클릭 이벤트
                                        },
                                        leadingIcon = {
                                            Icon(
                                                Icons.Default.Edit,
                                                contentDescription = "Edit",
                                                tint = Color.Gray
                                            )
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = {
                                            Text("삭제하기", color = Color.Red)
                                        },
                                        onClick = {
                                            expanded = false
                                            onDeleteClick() // 삭제하기 클릭 이벤트
                                        },
                                        leadingIcon = {
                                            Icon(
                                                Icons.Default.Delete,
                                                contentDescription = "Delete",
                                                tint = Color.Red
                                            )
                                        }
                                    )
                                }

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
                                text = element.roadAddress,
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
                                text = element.tags.joinToString(", "),
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

@RequiresApi(Build.VERSION_CODES.O)
fun formatTimestamp(timestamp: Long): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        .withZone(ZoneId.systemDefault()) // 시스템 기본 시간대
    return formatter.format(Instant.ofEpochMilli(timestamp))
}