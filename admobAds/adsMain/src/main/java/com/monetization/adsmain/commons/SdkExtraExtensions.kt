package com.monetization.adsmain.commons

import android.app.Activity
import com.example.rewadedad.AdmobRewardedAdsController
import com.example.rewadedad.AdmobRewardedAdsManager
import com.example.rewardedinterads.AdmobRewardedInterAdsController
import com.example.rewardedinterads.AdmobRewardedInterAdsManager
import com.monetization.appopen.AdmobAppOpenAdsController
import com.monetization.appopen.AdmobAppOpenAdsManager
import com.monetization.bannerads.AdmobBannerAdsController
import com.monetization.bannerads.AdmobBannerAdsManager
import com.monetization.core.ad_units.core.AdType
import com.monetization.core.controllers.AdsController
import com.monetization.core.listeners.ControllersListener
import com.monetization.core.managers.AdsLoadingStatusListener
import com.monetization.interstitials.AdmobInterstitialAdsController
import com.monetization.interstitials.AdmobInterstitialAdsManager
import com.monetization.nativeads.AdmobNativeAdsController
import com.monetization.nativeads.AdmobNativeAdsManager


fun String.isAdRequesting(adType: AdType): Boolean {
    return getAdController(adType)?.isAdRequesting() ?: false
}

fun String.loadAd(adType: AdType, activity: Activity, listener: AdsLoadingStatusListener? = null) {
    getAdController(adType)?.loadAd(activity, "", listener)
}

fun String.destroyAd(adType: AdType, activity: Activity? = null) {
    getAdController(adType)?.destroyAd(activity)
}

fun AdType.loadAd(key: String, activity: Activity, listener: AdsLoadingStatusListener? = null) {
    getAdController(key)?.loadAd(activity, "", listener)
}


fun String.isAdAvailable(adType: AdType): Boolean {
    return getAdController(adType)?.isAdAvailable() ?: false
}

fun String.getAdIdToRequest(adType: AdType): String? {
    return getAdController(adType)?.getAdId()
}

fun String.getAdController(adType: AdType): AdsController? {
    return adType.getAdController(this)
}

fun AdmobInterstitialAdsManager.addNewController(
    adKey: String,
    adIdsList: List<String>,
    listener: ControllersListener? = null
) {
    addNewController(AdmobInterstitialAdsController(adKey, adIdsList, listener))
}

fun AdmobNativeAdsManager.addNewController(
    adKey: String,
    adIdsList: List<String>,
    listener: ControllersListener? = null
) {
    addNewController(AdmobNativeAdsController(adKey, adIdsList, listener))
}


fun AdmobBannerAdsManager.addNewController(
    adKey: String,
    adIdsList: List<String>,
    listener: ControllersListener? = null
) {
    addNewController(AdmobBannerAdsController(adKey, adIdsList, listener))
}

fun AdmobAppOpenAdsManager.addNewController(
    adKey: String,
    adIdsList: List<String>,
    listener: ControllersListener? = null
) {
    addNewController(AdmobAppOpenAdsController(adKey, adIdsList, listener))
}


fun AdmobRewardedAdsManager.addNewController(
    adKey: String,
    adIdsList: List<String>,
    listener: ControllersListener? = null
) {
    addNewController(AdmobRewardedAdsController(adKey, adIdsList, listener))
}


fun AdmobRewardedInterAdsManager.addNewController(
    adKey: String,
    adIdsList: List<String>,
    listener: ControllersListener? = null
) {
    addNewController(AdmobRewardedInterAdsController(adKey, adIdsList, listener))
}


fun AdType.getAdController(key: String): AdsController? {
    val manager = when (this) {
        AdType.NATIVE -> AdmobNativeAdsManager
        AdType.INTERSTITIAL -> AdmobInterstitialAdsManager
        AdType.REWARDED -> AdmobRewardedAdsManager
        AdType.REWARDED_INTERSTITIAL -> AdmobRewardedInterAdsManager
        AdType.BANNER -> AdmobBannerAdsManager
        AdType.AppOpen -> AdmobAppOpenAdsManager
    }
    return manager.getAdController(key)
}