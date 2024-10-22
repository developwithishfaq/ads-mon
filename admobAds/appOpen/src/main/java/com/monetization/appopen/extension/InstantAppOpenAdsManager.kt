package com.monetization.appopen.extension

import android.app.Activity
import com.monetization.appopen.AdmobAppOpenAd
import com.monetization.appopen.AdmobAppOpenAdsManager
import com.monetization.core.managers.AdmobBaseInstantAdsManager
import com.monetization.core.managers.FullScreenAdsShowListener
import com.monetization.core.ad_units.core.AdType
import com.monetization.core.commons.AdsCommons

object InstantAppOpenAdsManager : AdmobBaseInstantAdsManager(AdType.AppOpen) {


    fun showInstantAppOpenAd(
        enableKey: String,
        activity: Activity,
        key: String,
        normalLoadingTime: Long = 1_000,
        instantLoadingTime: Long = 8_000,
        requestNewIfAdShown: Boolean = false,
        onLoadingDialogStatusChange: (Boolean) -> Unit,
        showBlackBg: ((Boolean) -> Unit),
        onAdDismiss: ((Boolean) -> Unit)? = null,
    ) {
        val controller = AdmobAppOpenAdsManager.getAdController(key)
        canShowAd(
            activity = activity,
            placementKey = enableKey,
            normalLoadingTime = normalLoadingTime,
            instantLoadingTime = instantLoadingTime,
            controller = controller,
            onLoadingDialogStatusChange = onLoadingDialogStatusChange,
            onAdDismiss = onAdDismiss,
            showAd = {
                showBlackBg.invoke(true)
                (controller?.getAvailableAd() as? AdmobAppOpenAd)?.showAd(
                    activity,
                    object : FullScreenAdsShowListener {
                        override fun onAdDismiss(
                            adKey: String,
                            adShown: Boolean,
                            rewardEarned: Boolean
                        ) {
                            showBlackBg.invoke(false)
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





















