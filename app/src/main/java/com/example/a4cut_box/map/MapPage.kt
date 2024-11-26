package com.example.a4cut_box.map

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.a4cut_box.BuildConfig.KAKAO_MAP_KEY
import com.example.a4cut_box.R
import com.kakao.vectormap.AppKey
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.KakaoMapSdk
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraUpdateFactory
import java.lang.Exception


@Composable
fun MapPage(modifier: Modifier = Modifier) {

    val context = LocalContext.current
    val mapView = remember { MapView(context) }

    AndroidView(
        modifier = modifier,
        factory = {
            context ->
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
                            override fun onMapReady(p0: KakaoMap) {
                                val cameraUpdate = CameraUpdateFactory.newCenterPosition(LatLng.from(37.50318978422175, 126.95741994721672))

                                // KakaoMap의 labelManager에서 레이어를 가져옴
                                val layer = p0.labelManager?.layer

                                // 카메라를 지정된 위치로 이동
                                p0.moveCamera(cameraUpdate)
                            }
//                            override fun getPosition(): LatLng {
//                                // 현재 위치를 반환
//                                return LatLng.from(locationY, locationX)
//                            }
                        }
                    )
                }
        }
    )

}


