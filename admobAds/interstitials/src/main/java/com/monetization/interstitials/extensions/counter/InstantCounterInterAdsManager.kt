package com.monetization.interstitials.extensions.counter

import android.app.Activity
import com.monetization.core.counters.CounterManager
import com.monetization.core.counters.CounterManager.counterWrapper
import com.monetization.interstitials.extensions.InstantInterstitialAdsManager

object InstantCounterInterAdsManager {

    fun showInstantInterstitialAd(
        placementKey: String,
        activity: Activity,
        key: String,
        counterKey: String,
        normalLoadingTime: Long = 1_000,
        instantLoadingTime: Long = 8_000,
        requestNewIfAdShown: Boolean = false,
        onLoadingDialogStatusChange: (Boolean) -> Unit,
        onAdDismiss: (Boolean) -> Unit,
    ) {
        counterWrapper(counterKey, onAdDismiss) {
            InstantInterstitialAdsManager.showInstantInterstitialAd(
                placementKey = placementKey,
                activity = activity,
                key = key,
                normalLoadingTime = normalLoadingTime,
                instantLoadingTime = instantLoadingTime,
                requestNewIfAdShown = requestNewIfAdShown,
                onLoadingDialogStatusChange = onLoadingDialogStatusChange,
                onAdDismiss = { adShown ->
                    CounterManager.adShownCounterReact(counterKey, adShown)
                    onAdDismiss.invoke(adShown)
                }
            )
        }
    }
}