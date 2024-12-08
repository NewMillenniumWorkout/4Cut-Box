//package com.example.a4cut_box.map
//
//import android.annotation.SuppressLint
//import android.os.Bundle
//import android.util.Log
//import android.widget.Toast
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.viewinterop.AndroidView
//import com.example.a4cut_box.R
//import com.kakao.vectormap.KakaoMap
//import com.kakao.vectormap.KakaoMapReadyCallback
//import com.kakao.vectormap.LatLng
//import com.kakao.vectormap.MapLifeCycleCallback
//import com.kakao.vectormap.MapView
//import com.kakao.vectormap.camera.CameraUpdateFactory
//import com.kakao.vectormap.label.LabelOptions
//import com.kakao.vectormap.label.LabelStyle
//import com.kakao.vectormap.label.LabelStyles
//import com.kakao.vectormap.label.LabelTextBuilder
//import com.kakao.vectormap.label.LodLabel
//import com.kakao.vectormap.label.LodLabelLayer
//
//
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            MyApp()
//        }
//    }
//}
//
//@Composable
//fun MyApp() {
//    KakaoMapView()
//}
//
//@Composable
//fun KakaoMapView() {
//    val context = LocalContext.current
//    val mapView = remember {MapView(context)}
//
//    AndroidView(
//        modifier = Modifier.fillMaxSize(), // AndroidView의 높이 임의 설정
//        factory = { context ->
//            mapView.apply {
//                mapView.start(
//                    object : MapLifeCycleCallback() {
//                        // 지도 생명 주기 콜백: 지도가 파괴될 때 호출
//                        override fun onMapDestroy() {
//                            // 필자가 직접 만든 Toast생성 함수
//                            //makeToast(context = context, message = "지도를 불러오는데 실패했습니다.")
//                        }
//
//                        // 지도 생명 주기 콜백: 지도 로딩 중 에러가 발생했을 때 호출
//                        override fun onMapError(exception: Exception?) {
//                            // 필자가 직접 만든 Toast생성 함수
//                            //makeToast(context = context, message = "지도를 불러오는 중 알 수 없는 에러가 발생했습니다.\n onMapError: $exception")
//                        }
//                    },
//                    object : KakaoMapReadyCallback() {
//                        // KakaoMap이 준비되었을 때 호출
//                        @SuppressLint("UseCompatLoadingForDrawables")
//                        override fun onMapReady(kakaoMap: KakaoMap) {
//                            // 카메라를 (locationY, locationX) 위치로 이동시키는 업데이트 생성
//                            val cameraUpdate = CameraUpdateFactory.newCenterPosition(centerPosition)
//
//                            // 지도에 표시할 라벨의 스타일 설정
//                            var style = kakaoMap.labelManager?.addLabelStyles(LabelStyles.from(
//                                LabelStyle.from(R.drawable.test_icon)))
//                            if (!isHome) {
//                                style = kakaoMap.labelManager?.addLabelStyles(LabelStyles.from(LabelStyle.from(R.drawable.ic_medic_v1)))
//                            }
//                            Log.e("", "center ${centerPosition.latitude} ${centerPosition.longitude}")
//                            // 라벨 옵션을 설정하고 위치와 스타일을 적용
//                            val options = LabelOptions.from(centerPosition).setStyles(style)
//
//                            // KakaoMap의 labelManager에서 레이어를 가져옴
//                            val layer = kakaoMap.labelManager?.layer
//
//                            // 카메라를 지정된 위치로 이동
//                            kakaoMap.moveCamera(cameraUpdate)
//
//                            kakaoMap.setOnLodLabelClickListener(object : KakaoMap.OnLodLabelClickListener {
//                                override fun onLodLabelClicked(
//                                    kakaoMap: KakaoMap?,
//                                    lodLabelLayer: LodLabelLayer?,
//                                    lodLabel: LodLabel?
//                                ): Boolean {
//                                    lodLabel?.texts?.forEach { it ->
//                                        Log.e("", "lodLabel $it, ${it.length}")
//                                        clickLabelName = it
//                                    }
//
//                                    return false ;
//                                }
//                            })
//
//                            // 지도에 라벨을 추가
//                            layer?.addLabel(options)
//                            if (isHome) {
//                                searchKeyList.forEach { item ->
//                                    Log.e(
//                                        "",
//                                        "forEach ... ${item.placeName} ${item.roadAddressName} ${item.y} ${item.x}"
//                                    )
//                                    val lat = item.y.toDouble()
//                                    val lon = item.x.toDouble()
//                                    val style = LabelStyles.from(
//                                        LabelStyle.from(R.drawable.test_icon)
//                                            .setTextStyles(30, 0x000000)
//                                    )
//                                    val labelText =
//                                        LabelTextBuilder().setTexts(item.placeName)
//                                    val options = LabelOptions.from(LatLng.from(lat, lon))
//                                        .setClickable(true)
//                                        .setStyles(style).setTexts(labelText)
//                                    val lodLayer = kakaoMap.labelManager?.lodLayer
//                                    lodLayer?.addLodLabel(options)
//                                }
//                            }
//                        }
//
//                        override fun getPosition(): LatLng {
//                            // 현재 위치를 반환
//                            return centerPosition
//                        }
//                    },
//                )
//            }
//        },
//    )
//    )
//}
