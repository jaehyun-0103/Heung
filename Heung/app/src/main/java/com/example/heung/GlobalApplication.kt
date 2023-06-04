package com.example.heung

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // KaKao SDK  초기화
        KakaoSdk.init(this, "6de2823164e42a1f96eabbc998a1feb8")
    }
}