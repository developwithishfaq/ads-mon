package com.example.rewadedad.extensions.counter

import android.app.Activity
import com.example.rewadedad.extensions.PreloadRewardedAdsManager
import com.monetization.core.counters.CounterManager
import com.monetization.core.counters.CounterManager.counterWrapper

object PreloadCounterRewardedAdsManager {

    fun tryShowingRewardedAd(
        placementKey: String,
        key: String,
        counterKey: String? ,
        activity: Activity,
        requestNewIfNotAvailable: Boolean = true,
        requestNewIfAdShown: Boolean = true,
        normalLoadingTime: Long = 1000,
        onLoadingDialogStatusChange: (Boolean) -> Unit,
        onRewarded: (Boolean) -> Unit,
        onAdDismiss: (Boolean) -> Unit,
    ) {
        counterWrapper(
            counterEnable = !counterKey.isNullOrBlank(),
            key = counterKey,
            onDismiss = onAdDismiss
        ) {
            PreloadRewardedAdsManager.tryShowingRewardedAd(
                placementKey = placementKey,
                key = key,
                activity = activity,
                requestNewIfNotAvailable = requestNewIfNotAvailable,
                requestNewIfAdShown = requestNewIfAdShown,
                normalLoadingTime = normalLoadingTime,
                onLoadingDialogStatusChange = onLoadingDialogStatusChange,
                onRewarded = onRewarded,
                onAdDismiss = { adShown ->
                    CounterManager.adShownCounterReact(counterKey, adShown)
                    onAdDismiss.invoke(adShown)
                }
            )
        }
    }
}