package com.example.adsxml.base

import android.app.Application
import com.monetization.adsmain.splash.AdmobSplashAdController
import com.monetization.core.ad_units.core.AdType
import com.monetization.core.commons.SdkConfigs
import com.monetization.core.commons.placementToAdWidgetModel
import com.monetization.core.listeners.RemoteConfigsProvider
import com.monetization.core.listeners.SdkListener
import com.monetization.core.ui.AdsWidgetData
import com.remote.firebaseconfigs.SdkRemoteConfigController
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class BaseApp : Application() {

    override fun onCreate() {
        super.onCreate()

        val module = module {
            single {
                AdmobSplashAdController()
            }
        }
        startKoin {
            modules(module)
            androidContext(applicationContext)
        }

        SdkConfigs.setRemoteConfigsListener(object : RemoteConfigsProvider {
            override fun isAdEnabled(placementKey: String, adKey: String): Boolean {
                return SdkRemoteConfigController.getRemoteConfigBoolean(placementKey)
            }

            override fun getAdWidgetData(placementKey: String, adKey: String): AdsWidgetData? {
                return SdkRemoteConfigController.getRemoteConfigString(placementKey)
                    .placementToAdWidgetModel()
            }
        })
        SdkConfigs.setListener(listener = object : SdkListener {
            override fun canShowAd(adType: AdType, adKey: String): Boolean {
                return true
            }

            override fun canLoadAd(adType: AdType, adKey: String): Boolean {
                return true
            }
        }, testModeEnable = true)
    }

}