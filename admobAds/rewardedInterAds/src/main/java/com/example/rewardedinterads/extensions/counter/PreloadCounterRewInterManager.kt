package com.example.rewardedinterads.extensions.counter

import android.app.Activity
import com.example.rewardedinterads.extensions.PreloadRewardedInterAdsManager
import com.monetization.core.counters.CounterManager
import com.monetization.core.counters.CounterManager.counterWrapper

object PreloadCounterRewInterManager {

    fun tryShowingRewardedInterAd(
        placementKey: String,
        key: String,
        counterKey: String,
        activity: Activity,
        requestNewIfNotAvailable: Boolean = true,
        requestNewIfAdShown: Boolean = true,
        normalLoadingTime: Long = 1000,
        onLoadingDialogStatusChange: (Boolean) -> Unit,
        onRewarded: (Boolean) -> Unit,
        onAdDismiss: (Boolean) -> Unit,
    ) {
        counterWrapper(counterKey, onAdDismiss) {
            PreloadRewardedInterAdsManager.tryShowingRewardedInterAd(
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