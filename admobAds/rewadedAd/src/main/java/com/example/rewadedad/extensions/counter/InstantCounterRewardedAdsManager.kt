package com.example.rewadedad.extensions.counter

import android.app.Activity
import com.example.rewadedad.extensions.InstantRewardedAdsManager
import com.monetization.core.counters.CounterManager
import com.monetization.core.counters.CounterManager.counterWrapper

object InstantCounterRewardedAdsManager {

    fun showInstantRewardedAd(
        enableKey: String,
        activity: Activity,
        key: String,
        counterKey: String? ,
        normalLoadingTime: Long = 1_000,
        instantLoadingTime: Long = 8_000,
        requestNewIfAdShown: Boolean = false,
        onLoadingDialogStatusChange: (Boolean) -> Unit,
        onRewarded: (Boolean) -> Unit,
        onAdDismiss: (Boolean) -> Unit,
    ) {
        counterWrapper(
            counterEnable = !counterKey.isNullOrBlank(),
            key = counterKey,
            onDismiss = onAdDismiss
        ) {
            InstantRewardedAdsManager.showInstantRewardedAd(
                enableKey = enableKey,
                activity = activity,
                key = key,
                normalLoadingTime = normalLoadingTime,
                instantLoadingTime = instantLoadingTime,
                requestNewIfAdShown = requestNewIfAdShown,
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





















