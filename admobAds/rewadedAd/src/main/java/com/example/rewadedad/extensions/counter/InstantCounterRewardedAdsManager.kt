package com.example.rewadedad.extensions.counter

import android.app.Activity
import com.example.rewadedad.extensions.InstantRewardedAdsManager
import com.monetization.core.counters.CounterManager
import com.monetization.core.counters.CounterManager.counterWrapper
import com.monetization.core.msgs.MessagesType

object InstantCounterRewardedAdsManager {

    fun showInstantRewardedAd(
        placementKey: String,
        activity: Activity,
        key: String,
        counterKey: String? ,
        normalLoadingTime: Long = 1_000,
        instantLoadingTime: Long = 8_000,
        requestNewIfAdShown: Boolean = false,
        onLoadingDialogStatusChange: (Boolean) -> Unit,
        onRewarded: (Boolean) -> Unit,
        onAdDismiss: (Boolean,MessagesType?) -> Unit,
    ) {
        counterWrapper(
            counterEnable = !counterKey.isNullOrBlank(),
            key = counterKey,
            onDismiss = onAdDismiss
        ) {
            InstantRewardedAdsManager.showInstantRewardedAd(
                placementKey = placementKey,
                activity = activity,
                key = key,
                normalLoadingTime = normalLoadingTime,
                instantLoadingTime = instantLoadingTime,
                requestNewIfAdShown = requestNewIfAdShown,
                onLoadingDialogStatusChange = onLoadingDialogStatusChange,
                onRewarded = onRewarded,
                onAdDismiss = { adShown,msg ->
                    CounterManager.adShownCounterReact(counterKey, adShown)
                    onAdDismiss.invoke(adShown,msg)
                }
            )
        }
    }
}





















