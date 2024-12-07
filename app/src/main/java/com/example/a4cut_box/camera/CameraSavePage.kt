package com.example.a4cut_box.camera

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a4cut_box.R
import com.example.a4cut_box.ui.theme.BoxBlack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraSavePage(modifier: Modifier = Modifier, onClickBack: () -> Unit) {
    val scrollState0 = rememberScrollState()
    val scrollState1 = rememberScrollState()
    var location by remember { mutableStateOf("동작구 상도동") }
    var memo by remember { mutableStateOf("") }
    var tagList = remember { mutableStateListOf("김민식", "김여진", "장민석", "이훈의", "고지흔", "조성현") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                ),
                title = {
                    Text(
                        "2024.10.27",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onClickBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Back",
                            modifier = Modifier.size(32.dp)
                        )
                    }
                })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(horizontal = 40.dp)
                .fillMaxSize()
                .verticalScroll(scrollState0)
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Image(
                painter = painterResource(id = R.drawable.photo_example), "photo",
                modifier = Modifier
                    .fillMaxWidth(),
                contentScale = ContentScale.FillWidth
            )
            Spacer(modifier = Modifier.size(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    imageVector = Icons.Default.Place,
                    contentDescription = null,
                    tint = Color.Gray
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = location, color = Color.Gray)
            }
            Spacer(modifier = Modifier.size(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "메모",
                    style = TextStyle(
                        fontSize = 20.sp,
                    )
                )
            }
            Spacer(modifier = Modifier.size(12.dp))
            OutlinedTextField(
                value = memo,
                onValueChange = { memo = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeightIn(min = 120.dp, max = 200.dp),
                placeholder = {
                    Text(text = "메모를 입력하세요.")
                },
                maxLines = 5,
                singleLine = false,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFCCCCCC),
                    unfocusedBorderColor = Color(0xFFCCCCCC)
                ),
                shape = RoundedCornerShape(16.dp)
            )
            Spacer(modifier = Modifier.size(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Tag",
                    style = TextStyle(
                        fontSize = 20.sp,
                    )
                )
            }
            Spacer(modifier = Modifier.size(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            )
            {
                OutlinedTextField(
                    value = memo,
                    onValueChange = { memo = it },
                    placeholder = {
                        Text(text = "테그를 입력하세요.")
                    },
                    modifier = Modifier
                        .weight(1f)
                        .requiredHeightIn(min = 56.dp, max = 56.dp),
                    maxLines = 1,
                    singleLine = false,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFCCCCCC),
                        unfocusedBorderColor = Color(0xFFCCCCCC)
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Box(
                    Modifier
                        .size(56.dp)
                        .clickable(onClick = {})
                ) {
                    Image(
                        painter = painterResource(R.drawable.tag_plus),
                        contentDescription = null,
                        modifier = Modifier.size(56.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            Spacer(modifier = Modifier.size(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 2.dp)
                    .horizontalScroll(scrollState1),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                tagList.forEach { tag -> TagElement(tag) { tagList.remove(tag) } }
            }
        }
    }
}

@Composable
fun TagElement(tagText: String, onClickX: () -> Unit) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(BoxBlack)
            .padding(vertical = 2.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(text = tagText, color = Color.White, fontSize = 20.sp)
        Spacer(modifier = Modifier.size(2.dp))
        Box(
            modifier = Modifier
                .size(16.dp)
                .clickable(onClick = onClickX)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "x",
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}