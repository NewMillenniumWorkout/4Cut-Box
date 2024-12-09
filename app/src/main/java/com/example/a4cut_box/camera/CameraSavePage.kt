package com.example.a4cut_box.camera

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
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
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.a4cut_box.R
import com.example.a4cut_box.model.FeatureViewModel
import com.example.a4cut_box.model.LocationViewModel
import com.example.a4cut_box.ui.theme.BoxBlack
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

fun getRoadAddress(context: Context, latitude: Double, longitude: Double): String {
    val geocoder = Geocoder(context, Locale("ko"))
    val addressList: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)

    Log.d("me", addressList.toString())
    return if (!addressList.isNullOrEmpty()) {
        addressList[0].getAddressLine(0)
    } else {
        "주소를 찾을 수 없습니다."
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun CameraSavePage(
    modifier: Modifier = Modifier,
    imageUri: String,
    onClickBack: () -> Unit,
    onClickSave: () -> Unit,
    featureViewModel: FeatureViewModel
) {
    // 위치 권한 설정
    val locationPermission = android.Manifest.permission.ACCESS_FINE_LOCATION
    val permissionState = rememberPermissionState(locationPermission)

    // 퍼미션 체크 및 처리
    if (!permissionState.status.isGranted) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("위치 권한이 필요합니다.")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { permissionState.launchPermissionRequest() }) {
                Text("권한 요청")
            }
        }
        return
    }

    val scrollState0 = rememberScrollState()
    val scrollState1 = rememberScrollState()
    var location by remember { mutableStateOf("") }
    var latitude by remember { mutableDoubleStateOf(0.0) }
    var longitude by remember { mutableDoubleStateOf(0.0) }
    var memo by remember { mutableStateOf("") }
    var tagInput by remember { mutableStateOf("") }
    val tagList = remember { mutableStateListOf<String>() }
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val locationViewModel: LocationViewModel = viewModel()
    val isUploading by featureViewModel.isUploading.collectAsState()
    var isUploadStarted by remember { mutableStateOf(false) }


    locationViewModel.init(context)
    LaunchedEffect(key1 = location) {
        locationViewModel.getCurrentLocation { loc ->
            val address = getRoadAddress(context, loc.latitude, loc.longitude)
            location = address
            latitude = loc.latitude
            longitude = loc.longitude
        }
    }

    LaunchedEffect(key1 = isUploading) {
        if (isUploadStarted && !isUploading) {
            onClickSave()
        }
    }


    if (isUploading) {
        androidx.compose.ui.window.Dialog(onDismissRequest = { }) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        }
    } else {
        val currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))

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
                            currentDate,
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
                Log.d("me", imageUri)
                Image(
                    painter = rememberAsyncImagePainter(
                        model = imageUri,
                        placeholder = painterResource(R.drawable.loading),
                    ),
                    contentDescription = null,
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
                        text = "제목",
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
                        Text(text = "제목을 입력하세요.")
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
                        value = tagInput,
                        onValueChange = { tagInput = it.filterNot { char -> char.isWhitespace() } },
                        placeholder = {
                            Text(text = "태그를 입력하세요.")
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
                            .size(50.dp)
                            .clickable(onClick = {
                                if (!tagList.contains(tagInput)) {
                                    tagList.add(tagInput)
                                }
                                tagInput = ""
                                focusManager.clearFocus() // 입력창 포커스 제거
                            })
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
                Spacer(modifier = Modifier.size(52.dp))
                Box(
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .fillMaxWidth()
                        .clickable(onClick = {
                            isUploadStarted = true
                            featureViewModel.sendElement(
                                imageUri = imageUri,
                                roadAddress = location,
                                longitude = longitude,
                                latitude = latitude,
                                memo = memo,
                                tags = tagList
                            )
//                            onClickSave()
                        })
                        .clip(RoundedCornerShape(8.dp))
                        .background(BoxBlack),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "저장하기",
                        color = Color.White,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(12.dp)
                    )
                }
                Spacer(modifier = Modifier.size(104.dp))
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
        Text(text = tagText, color = Color.White, fontSize = 16.sp)
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