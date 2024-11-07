package com.monetization.adsmain.commons

/*


fun showFullScreenAd(
    adType: AdType,
    placementKey: String,
    activity: Activity,
    key: String,
    counterKey: String?,
    normalLoadingTime: Long = 1_000,
    instantLoadingTime: Long = 8_000,
    isInstantAd: Boolean = false,
    requestNewIfAdShown: Boolean = false,
    showBlackBg: ((Boolean) -> Unit)? = null,
    onLoadingDialogStatusChange: (Boolean) -> Unit,
    onRewarded: ((Boolean) -> Unit)? = null,
    onAdDismiss: (Boolean) -> Unit,
) {
    when (adType) {
        AdType.INTERSTITIAL -> {
            if (isInstantAd) {
                InstantCounterInterAdsManager.showInstantInterstitialAd(
                    placementKey = placementKey,
                    activity = activity,
                    key = key,
                    normalLoadingTime = normalLoadingTime,
                    instantLoadingTime = instantLoadingTime,
                    requestNewIfAdShown = requestNewIfAdShown,
                    onLoadingDialogStatusChange = onLoadingDialogStatusChange,
                    counterKey = counterKey,
                )
            }else{
                PreloadCounterInterAdsManager
            }
        }

        AdType.REWARDED -> {

        }

        AdType.REWARDED_INTERSTITIAL -> {

        }

        AdType.AppOpen -> {

        }

        else -> {
            logAds("Invalid Full Screen Ad Type=$adType")
            onAdDismiss.invoke(false)
        }
    }
}*/
