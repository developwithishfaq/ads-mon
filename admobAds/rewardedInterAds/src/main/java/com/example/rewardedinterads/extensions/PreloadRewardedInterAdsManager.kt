package com.example.rewardedinterads.extensions

import android.app.Activity
import com.example.rewardedinterads.AdmobRewardedInterAd
import com.example.rewardedinterads.AdmobRewardedInterAdsManager
import com.monetization.core.managers.AdmobBasePreloadAdsManager
import com.monetization.core.managers.FullScreenAdsShowListener
import com.monetization.core.ad_units.core.AdType
import com.monetization.core.commons.AdsCommons

object PreloadRewardedInterAdsManager : AdmobBasePreloadAdsManager(AdType.REWARDED_INTERSTITIAL) {

    fun tryShowingRewardedInterAd(
        placementKey: String,
        key: String,
        activity: Activity,
        requestNewIfNotAvailable: Boolean = true,
        requestNewIfAdShown: Boolean = true,
        normalLoadingTime: Long = 1000,
        onLoadingDialogStatusChange: (Boolean) -> Unit,
        onRewarded: (Boolean) -> Unit,
        onAdDismiss: ((Boolean) -> Unit)? = null,
    ) {

        val controller = AdmobRewardedInterAdsManager.getAdController(key)
        canShowAd(
            activity = activity,
            requestNewIfNotAvailable = requestNewIfNotAvailable,
            placementKey = placementKey,
            normalLoadingTime = normalLoadingTime,
            controller = controller,
            onLoadingDialogStatusChange = onLoadingDialogStatusChange,
            onAdDismiss = onAdDismiss,
            showAd = {
                (controller?.getAvailableAd() as? AdmobRewardedInterAd)?.showAd(
                    activity, object : FullScreenAdsShowListener {
                        override fun onAdDismiss(
                            adKey: String,
                            adShown: Boolean,
                            rewardEarned: Boolean,
                        ) {
                            onRewarded.invoke(rewardEarned)
                            onFreeAd(adShown)
                            if (requestNewIfAdShown) {
                                controller.loadAd(activity, "", null)
                            }
                        }
                    }
                )
            }
        )
    }
}