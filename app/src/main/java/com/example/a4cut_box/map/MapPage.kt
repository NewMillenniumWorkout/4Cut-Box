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

    // 허용 오차 설정 (약 0.0001 정도면 대략적인 몇 미터 단위)
    val tolerance = 0.0001

    // 요소들을 클러스터링 (유사한 위치끼리 그룹화)
    val groupedElements = remember(elements) {
        clusterElements(elements, tolerance)
    }

    LaunchedEffect(Unit) {
        featureViewModel.listenForElement()
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
                            // 필요한 경우 자원 정리
                        }

                        override fun onMapError(p0: Exception?) {
                            // 에러 처리
                            p0?.printStackTrace()
                        }
                    },
                    object : KakaoMapReadyCallback() {
                        override fun onMapReady(kakaoMap: KakaoMap) {

                            // 지도 중심 위치 설정
                            val cameraUpdate = CameraUpdateFactory.newCenterPosition(
                                userLatLng
                            )
                            kakaoMap.moveCamera(cameraUpdate)
                            kakaoMap.setGestureEnable(GestureType.OneFingerDoubleTap, true)
                            kakaoMap.setGestureEnable(GestureType.TwoFingerSingleTap, true)
                            kakaoMap.setGestureEnable(GestureType.Zoom, true)

                            addCustomMarkers(kakaoMap, groupedElements)

                            kakaoMap.setOnLabelClickListener(object :
                                KakaoMap.OnLabelClickListener {
                                override fun onLabelClicked(
                                    kakaoMap: KakaoMap?,
                                    labelLayer: LabelLayer,
                                    label: Label?
                                ): Boolean {
                                    val latLng = label?.position ?: return false
                                    selectedElements =
                                        findClusterElements(groupedElements, latLng, tolerance)
                                    val centerUpdate = CameraUpdateFactory.newCenterPosition(latLng)
                                    kakaoMap?.moveCamera(centerUpdate)
                                    if (selectedElements.isNotEmpty()) {
                                        showBubble = true
                                    }
                                    return false
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
        ) {
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
        val labelOptions = LabelOptions.from(latLng)
            .setStyles(styles)

        kakaoMap.labelManager?.layer?.addLabel(labelOptions)?.let {
            Log.d("MapPage", "Label added with ID: ${it.labelId}")
        }
    }
}

@Composable
fun BubbleDialog(navController: NavController, elements: List<Element>, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(24.dp))
                .background(color = BoxBlack)
                .padding(8.dp)
                .fillMaxWidth(0.8f)
                .fillMaxHeight(0.6f)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(elements) { element ->
                    AsyncImage(
                        model = element.imageUrl,
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

/**
 * elements를 받아서 특정 tolerance 이내에 위치한 요소들을 하나의 그룹으로 묶어주는 함수.
 */
private fun clusterElements(
    elements: List<Element>,
    tolerance: Double
): Map<LatLng, List<Element>> {
    val clusters = mutableListOf<MutableList<Element>>()

    elements.forEach { element ->
        val elementLatLng = LatLng.from(element.latitude, element.longitude)
        var added = false

        // 기존 클러스터에 속하는지 확인
        for (cluster in clusters) {
            // 클러스터 내 임의의 한 점과 비교하여 tolerance 이내면 같은 클러스터로 본다.
            val representative = cluster.first()
            val repLatLng = LatLng.from(representative.latitude, representative.longitude)
            if (isWithinTolerance(repLatLng, elementLatLng, tolerance)) {
                cluster.add(element)
                added = true
                break
            }
        }

        // 어떤 클러스터에도 속하지 않으면 새로운 클러스터 생성
        if (!added) {
            clusters.add(mutableListOf(element))
        }
    }

    // 각 클러스터의 대표 좌표를 평균으로 잡아서 Map<LatLng, List<Element>> 형태로 변환
    return clusters.associate { cluster ->
        val avgLat = cluster.map { it.latitude }.average()
        val avgLng = cluster.map { it.longitude }.average()
        LatLng.from(avgLat, avgLng) to cluster.toList()
    }
}

private fun isWithinTolerance(a: LatLng, b: LatLng, tolerance: Double): Boolean {
    return Math.abs(a.latitude - b.latitude) < tolerance && Math.abs(a.longitude - b.longitude) < tolerance
}

/**
 * 라벨 클릭 시 해당 라벨 주변 tolerance 내에 있는 클러스터를 찾아 반환
 */
private fun findClusterElements(
    groupedElements: Map<LatLng, List<Element>>,
    targetLatLng: LatLng,
    tolerance: Double
): List<Element> {
    groupedElements.forEach { (key, value) ->
        if (isWithinTolerance(key, targetLatLng, tolerance)) {
            return value
        }
    }
    return emptyList()
}
