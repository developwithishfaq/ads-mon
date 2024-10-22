package com.monetization.core.commons

import android.content.Context
import com.facebook.shimmer.BuildConfig
import com.monetization.core.ad_units.core.AdType
import com.monetization.core.commons.AdsCommons.logAds
import com.monetization.core.listeners.RemoteConfigsProvider
import com.monetization.core.listeners.SdkListener
import com.monetization.core.ui.AdsWidgetData
import java.util.UUID

object SdkConfigs {

    private var isTestModeEnabled = BuildConfig.DEBUG
    private var disableAllAds = false


    fun getUserId(context: Context): String {
        val prefs = context.getSharedPreferences("sdkPrefs", Context.MODE_PRIVATE)
        var userId = prefs.getString("userId", "") ?: ""
        if (userId.isNotBlank()) {
            return userId
        } else {
            userId = UUID.randomUUID().toString()
            prefs.edit().putString("userId", userId).apply()
            return userId
        }
    }

    fun isTestMode(): Boolean {
        return isTestModeEnabled
    }

    fun setTestModeEnabled(enable: Boolean) {
        isTestModeEnabled = enable
    }

    fun disableAllAds(disableAds: Boolean = true) {
        disableAllAds = disableAds
    }


    private var sdkListener: SdkListener? = null
    private var configListener: RemoteConfigsProvider? = null

    fun setRemoteConfigsListener(listener: RemoteConfigsProvider) {
        configListener = listener
    }

    fun String.isAdEnabled(key: String, def: Boolean = true): Boolean {
        if (configListener == null) {
            throw IllegalArgumentException("Please set Remote Config Listener by call setRemoteConfigsListener(this)")
        }
        return configListener?.isAdEnabled(this, key) ?: def
    }

    fun String.getAdWidgetModel(key:String,model: AdsWidgetData? = null): AdsWidgetData? {
        if (configListener == null) {
            throw IllegalArgumentException("Please set Remote Config Listener by call setRemoteConfigsListener(this)")
        }
        return configListener?.getAdWidgetData(this, key) ?: model
    }


    fun setListener(
        listener: SdkListener,
        testModeEnable: Boolean
    ) {
        sdkListener = listener
        setTestModeEnabled(testModeEnable)
    }

    fun getListener(): SdkListener? {
        if (sdkListener == null) {
            throw IllegalArgumentException("Please attach sdk listeners like SdkConfigs.setListener(this)")
        }
        return sdkListener
    }

    fun canShowAds(adKey: String, adType: AdType): Boolean {
        if (sdkListener == null) {
            throw IllegalArgumentException("Please attach sdk listeners like SdkConfigs.setListener(this)")
        } else if (disableAllAds) {
            logAds("All Ads Are Disabled by developer", true)
            return false
        } else {
            return sdkListener?.canShowAd(adType, adKey) ?: false
        }
    }

    fun canLoadAds(adKey: String, adType: AdType): Boolean {
        if (sdkListener == null) {
            throw IllegalArgumentException("Please attach sdk listeners like SdkConfigs.setListener(this)")
        } else if (disableAllAds) {
            logAds("All Ads Are Disabled by developer", true)
            return false
        } else {
            return sdkListener?.canLoadAd(adType, adKey) ?: false
        }
    }

}