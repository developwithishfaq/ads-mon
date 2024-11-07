package com.monetization.adsmain.showRates.full_screen_ads

import android.app.Activity
import com.example.rewadedad.extensions.counter.InstantCounterRewardedAdsManager
import com.example.rewardedinterads.extensions.counter.PreloadCounterRewInterManager
import com.monetization.appopen.extension.InstantAppOpenAdsManager
import com.monetization.appopen.extension.PreloadAppOpenAdsManager
import com.monetization.core.ad_units.core.AdType
import com.monetization.core.commons.AdsCommons.logAds
import com.monetization.core.msgs.MessagesType
import com.monetization.interstitials.extensions.counter.InstantCounterInterAdsManager
import com.monetization.interstitials.extensions.counter.PreloadCounterInterAdsManager

object FullScreenAdsShowManager {

    fun showFullScreenAd(
        placementKey: String,
        activity: Activity,
        key: String,
        counterKey: String?,
        adType: AdType,
        isInstantAd: Boolean,
        normalLoadingTime: Long = 1_000,
        instantLoadingTime: Long = 8_000,
        onLoadingDialogStatusChange: (Boolean) -> Unit,
        onRewarded: ((Boolean) -> Unit)? = null,
        showBlackBg: ((Boolean) -> Unit)? = null,
        onAdDismiss: (Boolean, MessagesType?) -> Unit,
    ) {
        when (adType) {
            AdType.INTERSTITIAL -> {
                if (isInstantAd) {
                    InstantCounterInterAdsManager.showInstantInterstitialAd(
                        placementKey = placementKey,
                        activity = activity,
                        key = key,
                        counterKey = counterKey,
                        normalLoadingTime = normalLoadingTime,
                        instantLoadingTime = instantLoadingTime,
                        requestNewIfAdShown = false,
                        onLoadingDialogStatusChange = onLoadingDialogStatusChange,
                        onAdDismiss = onAdDismiss
                    )
                } else {
                    PreloadCounterInterAdsManager.tryShowingInterstitialAd(
                        placementKey = placementKey,
                        key = key,
                        counterKey = counterKey,
                        activity = activity,
                        requestNewIfNotAvailable = false,
                        requestNewIfAdShown = false,
                        normalLoadingTime = normalLoadingTime,
                        onLoadingDialogStatusChange = onLoadingDialogStatusChange,
                        onAdDismiss = onAdDismiss
                    )
                }
            }

            AdType.REWARDED -> {
                if (isInstantAd) {
                    InstantCounterInterAdsManager.showInstantInterstitialAd(
                        placementKey = placementKey,
                        activity = activity,
                        key = key,
                        counterKey = counterKey,
                        normalLoadingTime = normalLoadingTime,
                        instantLoadingTime = instantLoadingTime,
                        requestNewIfAdShown = false,
                        onLoadingDialogStatusChange = onLoadingDialogStatusChange,
                        onAdDismiss = onAdDismiss
                    )
                } else {
                    PreloadCounterInterAdsManager.tryShowingInterstitialAd(
                        placementKey = placementKey,
                        key = key,
                        counterKey = counterKey,
                        activity = activity,
                        requestNewIfNotAvailable = false,
                        requestNewIfAdShown = false,
                        normalLoadingTime = normalLoadingTime,
                        onLoadingDialogStatusChange = onLoadingDialogStatusChange,
                        onAdDismiss = onAdDismiss
                    )
                }
            }

            AdType.REWARDED_INTERSTITIAL -> {
                if (isInstantAd) {
                    InstantCounterRewardedAdsManager.showInstantRewardedAd(
                        placementKey = placementKey,
                        activity = activity,
                        key = key,
                        counterKey = counterKey,
                        normalLoadingTime = normalLoadingTime,
                        instantLoadingTime = instantLoadingTime,
                        requestNewIfAdShown = false,
                        onLoadingDialogStatusChange = onLoadingDialogStatusChange,
                        onAdDismiss = onAdDismiss,
                        onRewarded = {
                            onRewarded?.invoke(it)
                        }
                    )
                } else {
                    PreloadCounterRewInterManager.tryShowingRewardedInterAd(
                        placementKey = placementKey,
                        key = key,
                        counterKey = counterKey,
                        activity = activity,
                        requestNewIfNotAvailable = false,
                        requestNewIfAdShown = false,
                        normalLoadingTime = normalLoadingTime,
                        onLoadingDialogStatusChange = onLoadingDialogStatusChange,
                        onAdDismiss = onAdDismiss,
                        onRewarded = {
                            onRewarded?.invoke(it)
                        }
                    )
                }
            }

            AdType.AppOpen -> {
                if (isInstantAd) {
                    InstantAppOpenAdsManager.showInstantAppOpenAd(
                        placementKey = placementKey,
                        activity = activity,
                        key = key,
                        normalLoadingTime = normalLoadingTime,
                        instantLoadingTime = instantLoadingTime,
                        requestNewIfAdShown = false,
                        onLoadingDialogStatusChange = onLoadingDialogStatusChange,
                        onAdDismiss = onAdDismiss,
                        showBlackBg = {
                            showBlackBg?.invoke(it)
                        }
                    )
                } else {
                    PreloadAppOpenAdsManager.tryShowingAppOpenAd(
                        placementKey = placementKey,
                        key = key,
                        activity = activity,
                        requestNewIfNotAvailable = false,
                        requestNewIfAdShown = false,
                        normalLoadingTime = normalLoadingTime,
                        onLoadingDialogStatusChange = onLoadingDialogStatusChange,
                        onAdDismiss = onAdDismiss,
                        showBlackBg = {
                            showBlackBg?.invoke(it)
                        }
                    )
                }
            }

            else -> {
                logAds("$adType is not a full screen ad", true)
            }
        }
    }
}