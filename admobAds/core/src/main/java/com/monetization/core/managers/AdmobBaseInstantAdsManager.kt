package com.monetization.core.managers

import android.app.Activity
import android.os.Handler
import android.os.Looper
import com.monetization.core.ad_units.core.AdType
import com.monetization.core.commons.AdsCommons
import com.monetization.core.commons.AdsCommons.isFullScreenAdShowing
import com.monetization.core.commons.AdsCommons.logAds
import com.monetization.core.commons.SdkConfigs
import com.monetization.core.commons.SdkConfigs.isAdEnabled
import com.monetization.core.controllers.AdsController

abstract class AdmobBaseInstantAdsManager(private val adType: AdType) {

    private var adHandler: Handler? = null

    private var loadingDialogListener: ((Boolean) -> Unit)? = null

    var onDismissListener: ((Boolean) -> Unit)? = {
        if (AdsCommons.isFullScreenAdShowing.not()) {
            stopHandler()
            onFreeAd(it)
        }
    }


    private var isHandlerRunning = false

    private val runnable = Runnable {
        if (isHandlerRunning && onDismissListener != null && AdsCommons.isFullScreenAdShowing.not()) {
            loadingDialogListener?.invoke(false)
            onFreeAd()
            onDismissListener = null
            stopHandler()
        }
    }

    fun onFreeAd(check: Boolean = false) {
        onDismissListener?.invoke(check)
        onDismissListener = null
        isFullScreenAdShowing = false
        stopHandler()
    }


    private fun startHandler(time: Long) {
        stopHandler()
        isHandlerRunning = true
        adHandler = Handler(Looper.getMainLooper())
        adHandler?.postDelayed(runnable, time)
    }

    fun stopHandler() {
        if (isHandlerRunning) {
            try {
                isHandlerRunning = false
                adHandler?.removeCallbacks(runnable)
                adHandler?.removeCallbacksAndMessages(null)
                adHandler = null
            } catch (_: Exception) {
            }
        }
    }

    fun canShowAd(
        activity: Activity,
        placementKey: String,
        normalLoadingTime: Long = 1_000,
        instantLoadingTime: Long = 8_000,
        controller: AdsController?,
        onLoadingDialogStatusChange: (Boolean) -> Unit,
        onAdDismiss: ((Boolean) -> Unit)? = null,
        showAd: () -> Unit,
    ) {
        val key = controller?.getAdKey() ?: ""
        val enable = placementKey.isAdEnabled(key)
        if (AdsCommons.isFullScreenAdShowing) {
            logAds("Full Screen Ad is already showing")
            return
        }
        loadingDialogListener = onLoadingDialogStatusChange
        onDismissListener = onAdDismiss


        if (enable.not()) {
            logAds("Ad is not enabled Key=$key,placement=$placementKey,type=$adType", true)
            onFreeAd()
            return
        }
        if (controller == null) {
            logAds("No Controller Available Key=$key,type=$adType", true)
            onFreeAd()
            return
        }
        if (SdkConfigs.canShowAds(key, adType).not()) {
            logAds("Ad is restricted by Sdk to show Key=$key,type=$adType", true)
            onFreeAd()
            return
        }
        isHandlerRunning = false
        if (normalLoadingTime > 0 || instantLoadingTime > 0) {
            loadingDialogListener?.invoke(true)
        }
        nowShowAd(
            activity,
            normalLoadingTime,
            instantLoadingTime,
            controller,
            showAd = showAd
        )
    }

    private fun nowShowAd(
        activity: Activity,
        normalLoadingTime: Long,
        instantLoadingTime: Long,
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
            startHandler(instantLoadingTime)
            controller.loadAd(activity = activity,
                calledFrom = "showInstantAd ad to show null",
                callback = object : AdsLoadingStatusListener {
                    override fun onAdLoaded(adKey: String) {
                        loadingDialogListener?.invoke(false)
                        val newAd = controller.getAvailableAd()
                        logAds("showInstantAd onAdLoaded, Checks = ${onDismissListener != null && newAd != null && activity.isFinishing.not() && activity.isDestroyed.not()}")
                        if (onDismissListener != null && newAd != null && activity.isFinishing.not() && activity.isDestroyed.not()) {
                            showAd.invoke()
                        } else {
                            stopHandler()
                            onFreeAd()
                        }
                    }

                    override fun onAdFailedToLoad(adKey: String, message: String, code: Int) {
                        AdsCommons.logAds("showInstantAd onAdFailedToLoad $message,$code")
                        loadingDialogListener?.invoke(false)
                        stopHandler()
                        onFreeAd()
                    }
                })
        }
    }


}