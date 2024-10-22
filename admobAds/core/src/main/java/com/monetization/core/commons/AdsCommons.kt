package com.monetization.core.commons

import android.app.Activity
import android.util.Log
import com.facebook.shimmer.BuildConfig
import com.monetization.core.ad_units.core.AdType
import com.monetization.core.commons.SdkConfigs.isTestMode

object AdsCommons {

    var isDebugMode = BuildConfig.DEBUG
    var isFullScreenAdShowing = false
    fun logAds(message: String, isError: Boolean = false) {
        if (isError) {
            Log.e("adsPlugin", "Admob Ads:$message")
        } else {
            Log.d("adsPlugin", "Admob Ads:$message")
        }
    }


    fun Activity.getGoodName(): String {
        return localClassName.substringAfterLast(".")
    }

    fun getAdId(
        indexOfId: Int,
        adIdsList: List<String>,
        adType: AdType,
        newValue: (Int) -> Unit,
    ): String {
        if (indexOfId >= adIdsList.size - 1) {
            newValue.invoke(0)
        } else {
            newValue.invoke(indexOfId + 1)
        }

        val testId = when (adType) {
            AdType.NATIVE -> TestAds.TestNativeId
            AdType.INTERSTITIAL -> TestAds.TestInterId
            AdType.BANNER -> TestAds.TestBannerId
            AdType.AppOpen -> TestAds.TestAppOpenId
            AdType.REWARDED -> TestAds.TestRewardedId
            AdType.REWARDED_INTERSTITIAL -> TestAds.TestRewardedInterId
        }
        return if (isTestMode()) {
            testId
        } else {
            try {
                adIdsList[indexOfId]
            } catch (_: Exception) {
                adIdsList[0]
            }
        }
    }

}