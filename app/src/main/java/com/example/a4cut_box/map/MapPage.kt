package com.example.a4cut_box.map

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.a4cut_box.R
import com.example.a4cut_box.model.Element
import com.example.a4cut_box.model.FeatureViewModel
import com.example.a4cut_box.ui.theme.BoxBlack
import com.google.android.gms.location.LocationServices
import com.kakao.vectormap.GestureType
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.Label
import com.kakao.vectormap.label.LabelLayer
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import java.math.RoundingMode


@Composable
fun MapPage(
    modifier: Modifier = Modifier,
    navController: NavController,
    featureViewModel: FeatureViewModel
) {

    val elements = featureViewModel.elements.collectAsState().value
    val context = LocalContext.current
    val mapView = remember { MapView(context) }
    var showBubble by remember { mutableStateOf(false) }
    var userLatLng by remember {
        mutableStateOf(
            LatLng.from(
                37.50318978422175,
                126.95741994721672
            )
        )
    }
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    var selectedElements by remember { mutableStateOf<List<Element>>(emptyList()) }
    var groupedElements = remember { mutableMapOf<LatLng, List<Element>>() }

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // 권한 요청
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                100
            )
        } else {
            // 권한이 이미 부여된 경우
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    userLatLng = LatLng.from(location.latitude, location.longitude)
                }
            }
        }
    }

    AndroidView(
        modifier = modifier,
        factory = { context ->
            mapView.apply {
                mapView.start(
                    object : MapLifeCycleCallback() {
                        override fun onMapDestroy() {
                            TODO("Not yet implemented")
                        }

                        override fun onMapError(p0: Exception?) {
                            TODO("Not yet implemented")
                        }
                    },
                    object : KakaoMapReadyCallback() {
                        override fun onMapReady(kakaoMap: KakaoMap) {
//                            Log.d(
//                                "MapPage",
//                                "userLatLng: ${userLatLng.latitude}, ${userLatLng.longitude}"
//                            )

                            // 지도 중심 위치 설정
                            val cameraUpdate = CameraUpdateFactory.newCenterPosition(
                                userLatLng
                            )

                            // 카메라를 지정된 위치로 이동
                            kakaoMap.moveCamera(cameraUpdate)
                            kakaoMap.setGestureEnable(GestureType.OneFingerDoubleTap, true);
                            kakaoMap.setGestureEnable(GestureType.TwoFingerSingleTap, true);
                            kakaoMap.setGestureEnable(GestureType.Zoom, true);

                            groupedElements =
                                elements.groupBy {
                                    val lat =
                                        it.latitude.toBigDecimal().setScale(5, RoundingMode.HALF_UP)
                                            .toDouble()
                                    val lng = it.longitude.toBigDecimal()
                                        .setScale(5, RoundingMode.HALF_UP).toDouble()
                                    val latLng = LatLng.from(lat, lng)
                                    Log.d(
                                        "MapPage",
                                        "Grouping Element: ${it.id} at LatLng: $latLng"
                                    )
                                    latLng
                                }
                                    .toMutableMap()
                            addCustomMarkers(kakaoMap, groupedElements)

                            kakaoMap.setOnLabelClickListener(object :
                                KakaoMap.OnLabelClickListener {
                                override fun onLabelClicked(
                                    kakaoMap: KakaoMap?,
                                    labelLayer: LabelLayer,
                                    label: Label?
                                ): Boolean {
                                    val latLng = label?.position ?: return false
                                    Log.d("MapPage", "Clicked Label LatLng: $latLng")
                                    selectedElements = findMatchingElements(groupedElements, latLng)
                                    Log.d(
                                        "MapPage",
                                        "Selected elements count: ${selectedElements.size}"
                                    )
                                    selectedElements.forEach {
                                        Log.d("MapPage", "Selected Element: ${it.imageUrl}")
                                    }
                                    val centerUpdate = CameraUpdateFactory.newCenterPosition(latLng)
                                    kakaoMap?.moveCamera(centerUpdate)
                                    if (selectedElements.isNotEmpty()) {
                                        showBubble = true
                                    }
                                    return false;
                                }
                            })
                        }

                    }
                )
            }
        }
    )

    // 말풍선 UI
    if (showBubble) {
        BubbleDialog(
            navController = navController,
            elements = selectedElements
        )
        {
            showBubble = false // 말풍선 닫기
            selectedElements = emptyList()
        }
    }

}


private fun addCustomMarkers(
    kakaoMap: KakaoMap,
    groupedElements: Map<LatLng, List<Element>>,
) {
    val styles = kakaoMap.labelManager
        ?.addLabelStyles(LabelStyles.from(LabelStyle.from(R.drawable.photo_label)))

    groupedElements.forEach { (latLng, group) ->
        Log.d("MapPage", "Adding Label at LatLng: $latLng with ${group.size} elements")

        // 라벨 생성
        val labelOptions = LabelOptions.from(latLng)
            .setStyles(styles)

        // 라벨 추가
        kakaoMap.labelManager?.layer?.addLabel(labelOptions)?.let {
            Log.d("MapPage", "Label added with ID: ${it.labelId}")
        }

    }
}

@Composable
fun BubbleDialog(navController: NavController, elements: List<Element>, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Log.d("MapPage", "BubbleDialog with ${elements.size} elements")
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(24.dp))
                .background(color = BoxBlack)
                .padding(8.dp)
                .fillMaxWidth(0.8f)
                .fillMaxHeight(0.6f)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2), // 2열
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(elements) { element ->
                    Log.d("MapPage", "Rendering element image URL: ${element.imageUrl}")
                    AsyncImage(
                        model = element.imageUrl, // URL에서 이미지 로드
                        contentDescription = "Element Image",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .border(2.dp, Color.White, RoundedCornerShape(16.dp))
                            .clickable {
                                navController.navigate("photoDetail/${element.id}")
                            },
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}

private fun findMatchingElements(
    groupedElements: Map<LatLng, List<Element>>,
    targetLatLng: LatLng,
    tolerance: Double = 0.0001 // 허용 오차
): List<Element> {
    groupedElements.forEach { (key, value) ->
        Log.d(
            "MapPage",
            "Checking LatLng: $key against Target: $targetLatLng with Tolerance: $tolerance"
        )
        if (Math.abs(key.latitude - targetLatLng.latitude) < tolerance &&
            Math.abs(key.longitude - targetLatLng.longitude) < tolerance
        ) {
            Log.d("MapPage", "Match found! Key: $key, Elements: ${value.map { it.imageUrl }}")
            return value
        }
    }
    Log.d("MapPage", "No matching elements found for LatLng: $targetLatLng")
    return emptyList()
}

