package com.example.rewadedad.extensions

import android.app.Activity
import com.example.rewadedad.AdmobRewardedAd
import com.example.rewadedad.AdmobRewardedAdsManager
import com.monetization.core.managers.AdmobBasePreloadAdsManager
import com.monetization.core.managers.FullScreenAdsShowListener
import com.monetization.core.ad_units.core.AdType
import com.monetization.core.commons.AdsCommons

object PreloadRewardedAdsManager : AdmobBasePreloadAdsManager(AdType.REWARDED) {

    fun tryShowingRewardedAd(
        placementKey: String,
        key: String,
        activity: Activity,
        requestNewIfNotAvailable: Boolean = true,
        requestNewIfAdShown: Boolean = true,
        normalLoadingTime: Long = 1000,
        onLoadingDialogStatusChange: (Boolean) -> Unit,
        onRewarded: (Boolean) -> Unit,
        onAdDismiss: (Boolean) -> Unit,
    ) {
        val controller = AdmobRewardedAdsManager.getAdController(key)
        canShowAd(
            activity = activity,
            requestNewIfNotAvailable = requestNewIfNotAvailable,
            placementKey = placementKey,
            normalLoadingTime = normalLoadingTime,
            controller = controller,
            onLoadingDialogStatusChange = onLoadingDialogStatusChange,
            onAdDismiss = onAdDismiss,
            showAd = {
                (controller?.getAvailableAd() as? AdmobRewardedAd)?.showAd(
                    activity, object : FullScreenAdsShowListener {
                        override fun onAdDismiss(
                            adKey: String,
                            adShown: Boolean,
                            rewardEarned: Boolean,
                        ) {
                            onRewarded.invoke(rewardEarned)
                            onFreeAd(adShown)
                            if (requestNewIfAdShown && adShown) {
                                controller.loadAd(activity, "", null)
                            }
                        }
                    }
                )
            }
        )
    }
    /*

        fun tryShowingRewardedAd(
            enable: Boolean,
            key: String,
            context: Activity,
            requestNewIfNotAvailable: Boolean = true,
            requestNewIfAdShown: Boolean = true,
            handlerDelay: Long = 1000,
            onLoadingDialogStatusChange: (Boolean) -> Unit,
            onRewarded: (Boolean) -> Unit,
            onAdDismiss: (Boolean) -> Unit,
        ) {
            if (AdsCommons.isFullScreenAdShowing) {
                return
            }
            if (enable.not()) {
                onAdDismiss.invoke(false)
                return
            }
            val controller = AdmobRewardedAdsManager.getAdController(key)
            if (controller == null) {
                onAdDismiss.invoke(false)
                return
            }
            val adToShow = (controller.getAvailableAd() as? GeneralInterOrAppOpenAd)
            if (adToShow != null) {
                if (handlerDelay > 0) {
                    onLoadingDialogStatusChange.invoke(true)
                }
                Handler(Looper.getMainLooper()).postDelayed({
                    onLoadingDialogStatusChange.invoke(false)
                    adToShow.showAd(context, object : FullScreenAdsShowListener {
                        override fun onAdDismiss(adKey: String,adShown: Boolean, rewardEarned: Boolean) {
                            AdsCommons.logAds("onAdDismiss called, adShown $adShown rewardEarned $rewardEarned")
                            onRewarded.invoke(rewardEarned)
                            onAdDismiss.invoke(adShown)
                            if (requestNewIfAdShown) {
                                controller.loadAd(
                                    context,
                                    "tryShowingInterstitialAd showAd  onAdDismiss requestNewIfAdShown",
                                    null
                                )
                            }
                        }

                        override fun onAdShownFailed() {
                            super.onAdShownFailed()
                            onAdDismiss.invoke(false)
                        }
                    })
                }, handlerDelay)
            } else {
                if (requestNewIfNotAvailable) {
                    controller.loadAd(
                        context,
                        "tryShowingInterstitialAd showAd null else case requestNewIfNotAvailable",
                        null
                    )
                }
                onAdDismiss.invoke(false)
            }
        }*/
}