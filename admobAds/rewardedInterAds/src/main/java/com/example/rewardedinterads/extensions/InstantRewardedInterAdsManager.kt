package com.example.rewardedinterads.extensions

import android.app.Activity
import com.example.rewardedinterads.AdmobRewardedInterAd
import com.example.rewardedinterads.AdmobRewardedInterAdsManager
import com.monetization.core.managers.AdmobBaseInstantAdsManager
import com.monetization.core.managers.FullScreenAdsShowListener
import com.monetization.core.ad_units.core.AdType
import com.monetization.core.commons.AdsCommons

object InstantRewardedInterAdsManager : AdmobBaseInstantAdsManager(AdType.REWARDED_INTERSTITIAL) {


    fun showInstantRewardedInterstitialAd(
        enableKey: String,
        activity: Activity,
        key: String,
        normalLoadingTime: Long = 1_000,
        instantLoadingTime: Long = 8_000,
        requestNewIfAdShown: Boolean = false,
        onLoadingDialogStatusChange: (Boolean) -> Unit,
        onRewarded: (Boolean) -> Unit,
        onAdDismiss: ((Boolean) -> Unit)? = null,
    ) {
        val controller = AdmobRewardedInterAdsManager.getAdController(key)
        canShowAd(
            activity = activity,
            placementKey = enableKey,
            normalLoadingTime = normalLoadingTime,
            instantLoadingTime = instantLoadingTime,
            controller = controller,
            onLoadingDialogStatusChange = onLoadingDialogStatusChange,
            onAdDismiss = onAdDismiss,
            showAd = {
                (controller?.getAvailableAd() as? AdmobRewardedInterAd)?.showAd(
                    activity,
                    object : FullScreenAdsShowListener {
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
}





















