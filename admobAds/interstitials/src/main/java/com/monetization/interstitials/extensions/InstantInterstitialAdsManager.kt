package com.monetization.interstitials.extensions

import android.app.Activity
import com.monetization.core.ad_units.core.AdType
import com.monetization.core.commons.AdsCommons
import com.monetization.core.managers.AdmobBaseInstantAdsManager
import com.monetization.core.managers.FullScreenAdsShowListener
import com.monetization.core.msgs.MessagesType
import com.monetization.interstitials.AdmobInterstitialAd
import com.monetization.interstitials.AdmobInterstitialAdsManager

object InstantInterstitialAdsManager : AdmobBaseInstantAdsManager(AdType.INTERSTITIAL) {

    fun showInstantInterstitialAd(
        placementKey: String,
        activity: Activity,
        key: String,
        normalLoadingTime: Long = 1_000,
        instantLoadingTime: Long = 8_000,
        requestNewIfAdShown: Boolean = false,
        onLoadingDialogStatusChange: (Boolean) -> Unit,
        onAdDismiss: (Boolean, MessagesType?) -> Unit,
    ) {
        val controller = AdmobInterstitialAdsManager.getAdController(key)
        canShowAd(
            activity = activity,
            placementKey = placementKey,
            normalLoadingTime = normalLoadingTime,
            instantLoadingTime = instantLoadingTime,
            controller = controller,
            onLoadingDialogStatusChange = onLoadingDialogStatusChange,
            onAdDismiss = onAdDismiss,
            showAd = {
                (controller?.getAvailableAd() as? AdmobInterstitialAd)?.showAd(
                    activity = activity,
                    callBack = getShowListener(
                        requestNewIfAdShown = requestNewIfAdShown,
                        activity = activity,
                        controller = controller,
                        adType = AdType.INTERSTITIAL
                    )
                )
            }
        )
    }

}





















