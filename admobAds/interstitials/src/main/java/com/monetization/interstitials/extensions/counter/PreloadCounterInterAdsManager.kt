package com.monetization.interstitials.extensions.counter

import android.app.Activity
import com.monetization.core.counters.CounterManager
import com.monetization.core.counters.CounterManager.counterWrapper
import com.monetization.interstitials.extensions.PreloadInterstitialAdsManager

object PreloadCounterInterAdsManager {

    fun tryShowingInterstitialAd(
        placementKey: String,
        key: String,
        counterKey: String,
        activity: Activity,
        requestNewIfNotAvailable: Boolean = false,
        requestNewIfAdShown: Boolean = false,
        normalLoadingTime: Long = 1000,
        onLoadingDialogStatusChange: (Boolean) -> Unit,
        onAdDismiss: (Boolean) -> Unit,
    ) {
        counterWrapper(counterKey, onAdDismiss) {
            PreloadInterstitialAdsManager.tryShowingInterstitialAd(
                placementKey = placementKey,
                key = key,
                activity = activity,
                requestNewIfNotAvailable = requestNewIfNotAvailable,
                requestNewIfAdShown = requestNewIfAdShown,
                normalLoadingTime = normalLoadingTime,
                onLoadingDialogStatusChange = onLoadingDialogStatusChange,
                onAdDismiss = { adShown ->
                    CounterManager.adShownCounterReact(counterKey, adShown)
                    onAdDismiss.invoke(adShown)
                }
            )
        }
    }

}