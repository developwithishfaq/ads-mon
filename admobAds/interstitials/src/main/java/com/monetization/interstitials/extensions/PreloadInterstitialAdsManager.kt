package com.monetization.interstitials.extensions

import android.app.Activity
import com.monetization.core.ad_units.core.AdType
import com.monetization.core.commons.AdsCommons
import com.monetization.core.managers.AdmobBasePreloadAdsManager
import com.monetization.core.managers.FullScreenAdsShowListener
import com.monetization.interstitials.AdmobInterstitialAd
import com.monetization.interstitials.AdmobInterstitialAdsManager

object PreloadInterstitialAdsManager : AdmobBasePreloadAdsManager(AdType.INTERSTITIAL) {

    fun tryShowingInterstitialAd(
        placementKey: String,
        key: String,
        activity: Activity,
        requestNewIfNotAvailable: Boolean = false,
        requestNewIfAdShown: Boolean = false,
        normalLoadingTime: Long = 1000,
        onLoadingDialogStatusChange: (Boolean) -> Unit,
        onAdDismiss: (Boolean) -> Unit,
    ) {

        val controller = AdmobInterstitialAdsManager.getAdController(key)
        canShowAd(
            activity = activity,
            requestNewIfNotAvailable = requestNewIfNotAvailable,
            placementKey = placementKey,
            normalLoadingTime = normalLoadingTime,
            controller = controller,
            onLoadingDialogStatusChange = onLoadingDialogStatusChange,
            onAdDismiss = onAdDismiss,
            showAd = {
                (controller?.getAvailableAd() as? AdmobInterstitialAd)?.showAd(
                    activity, object : FullScreenAdsShowListener {
                        override fun onAdDismiss(
                            adKey: String,
                            adShown: Boolean,
                            rewardEarned: Boolean,
                        ) {
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

}