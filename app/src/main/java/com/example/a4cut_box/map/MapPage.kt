package com.example.a4cut_box.map

import androidx.compose.foundation.Image
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.a4cut_box.R
import com.example.a4cut_box.ui.theme.BoxBlack
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
fun MapPage(modifier: Modifier = Modifier, navController: NavController) {

    val context = LocalContext.current
    val mapView = remember { MapView(context) }
    var showBubble by remember { mutableStateOf(false) }

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
                            // 지도 중심 위치 설정
                            val cameraUpdate = CameraUpdateFactory.newCenterPosition(
                                LatLng.from(
                                    37.50318978422175,
                                    126.95741994721672
                                )
                            )

                            // KakaoMap의 labelManager에서 레이어를 가져옴
                            val layer = kakaoMap.labelManager?.layer

                            // 카메라를 지정된 위치로 이동
                            kakaoMap.moveCamera(cameraUpdate)
                            kakaoMap.setGestureEnable(GestureType.OneFingerDoubleTap, true);
                            kakaoMap.setGestureEnable(GestureType.TwoFingerSingleTap, true);
                            kakaoMap.setGestureEnable(GestureType.Zoom, true);

                            addCustomMarkers(kakaoMap)

                            kakaoMap.setOnLabelClickListener(object :
                                KakaoMap.OnLabelClickListener {
                                override fun onLabelClicked(
                                    kakaoMap: KakaoMap?,
                                    labelLayer: LabelLayer,
                                    label: Label?
                                ): Boolean {
                                    val latLng = label?.position ?: return false
                                    val centerUpdate = CameraUpdateFactory.newCenterPosition(latLng)
                                    kakaoMap?.moveCamera(centerUpdate)
                                    showBubble = true
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
        BubbleDialog(navController = navController) {
            showBubble = false // 말풍선 닫기
        }
    }

}


private fun addCustomMarkers(kakaoMap: KakaoMap) {
    val styles = kakaoMap.labelManager
        ?.addLabelStyles(LabelStyles.from(LabelStyle.from(R.drawable.photo_label)))

    // 2. LabelOptions 생성: 라벨의 위치와 스타일 설정
    val labelOptions = LabelOptions.from(LatLng.from(37.50318978422175, 126.95741994721672))
        .setStyles(styles)

    // 3. LabelLayer 가져오기
    val labelLayer = kakaoMap.labelManager?.layer

    // 4. LabelLayer에 LabelOptions를 사용하여 라벨 추가
    labelLayer?.addLabel(labelOptions)

}

@Composable
fun BubbleDialog(navController: NavController, onDismiss: () -> Unit) {
    val imageList = remember { List(5) { R.drawable.test_image } }

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
                columns = GridCells.Fixed(2), // 3열
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(imageList) { imageRes ->
                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = "Grid Image",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .border(2.dp, Color.White, RoundedCornerShape(16.dp))
                            .clickable {
                                navController.navigate("photoDetail")
                            },
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }

}

