package com.monetization.core.managers

import android.app.Activity
import android.os.Handler
import android.os.Looper
import com.monetization.core.ad_units.core.AdType
import com.monetization.core.commons.AdsCommons.isFullScreenAdShowing
import com.monetization.core.commons.AdsCommons.logAds
import com.monetization.core.commons.SdkConfigs
import com.monetization.core.commons.SdkConfigs.isAdEnabled
import com.monetization.core.controllers.AdsController

abstract class AdmobBasePreloadAdsManager(
    private val adType: AdType,
) {
    private var loadingDialogListener: ((Boolean) -> Unit)? = null

    private var onDismissListener: ((Boolean) -> Unit)? = null
    fun onFreeAd(check: Boolean = false) {
        onDismissListener?.invoke(check)
        onDismissListener = null
        isFullScreenAdShowing = false
    }

    private fun allowed(key: String): Boolean {
        val allowed = SdkConfigs.canShowAds(key, adType)
        if (allowed.not()) {
            logAds("Ad is restricted by Sdk to show Key=$key,type=$adType", true)
        }
        return allowed
    }

    fun canShowAd(
        activity: Activity,
        placementKey: String,
        normalLoadingTime: Long = 1_000,
        controller: AdsController?,
        requestNewIfNotAvailable: Boolean,
        onLoadingDialogStatusChange: (Boolean) -> Unit,
        onAdDismiss: ((Boolean) -> Unit)? = null,
        showAd: () -> Unit,
    ) {
        val key = controller?.getAdKey() ?: ""
        val enabled = placementKey.isAdEnabled(key)
        if (isFullScreenAdShowing) {
            logAds("Full Screen Ad is already showing", true)
            return
        }
        loadingDialogListener = onLoadingDialogStatusChange
        onDismissListener = onAdDismiss
        val availableAd = controller?.getAvailableAd()
        if (controller == null) {
            logAds("No Controller Found Against $key,$adType", true)
            onFreeAd()
            return
        }
        if (enabled.not()) {
            logAds("$adType:$key,is not enabled", true)
            onFreeAd()
            return
        }
        if (requestNewIfNotAvailable && availableAd == null) {
            logAds("$adType:$key,New Ad Request as no ad is available to show", true)
            controller.loadAd(activity, "", null)
            onFreeAd()
            return
        }
        if (availableAd == null) {
            logAds("$adType:$key, No Ad To Show, Please Request an ad First", true)
            onFreeAd()
            return
        }
        if (allowed(key).not()) {
            onFreeAd()
            return
        }
        if (normalLoadingTime > 0) {
            loadingDialogListener?.invoke(true)
        }
        nowShowAd(
            normalLoadingTime = normalLoadingTime,
            controller = controller,
            showAd = showAd
        )
    }

    private fun nowShowAd(
        normalLoadingTime: Long,
        controller: AdsController,
        showAd: () -> Unit,
    ) {
        val adToShow = controller.getAvailableAd()
        if (adToShow != null) {
            Handler(Looper.getMainLooper()).postDelayed({
                loadingDialogListener?.invoke(false)
                showAd.invoke()
            }, normalLoadingTime)
        } else {
            logAds("$adType:No Ad is available to show", true)
            loadingDialogListener?.invoke(false)
            onFreeAd()
        }
    }


}