package com.monetization.adsmain.commons

import android.app.Activity
import androidx.lifecycle.Lifecycle
import com.monetization.adsmain.widgets.AdsUiWidget
import com.monetization.bannerads.BannerAdSize
import com.monetization.bannerads.BannerAdType
import com.monetization.core.listeners.UiAdsListener
import com.monetization.core.ui.AdsWidgetData
import com.monetization.core.ui.LayoutInfo
import com.monetization.core.ui.ShimmerInfo


fun AdsUiWidget.sdkNativeAd(
    activity: Activity,
    adLayout: String,
    adKey: String,
    placementKey: String,
    lifecycle: Lifecycle,
    showShimmerLayout: ShimmerInfo = ShimmerInfo.GivenLayout(),
    requestNewOnShow: Boolean = false,
    showNewAdEveryTime: Boolean = true,
    defaultEnable: Boolean = true,
    adsWidgetData: AdsWidgetData? = null,
    listener: UiAdsListener? = null
) {
    apply {
        attachWithLifecycle(lifecycle = lifecycle, false)
        setWidgetKey(
            placementKey = placementKey,
            adKey = adKey,
            model = adsWidgetData,
            defEnabled = defaultEnable
        )
        showNativeAdmob(
            activity = activity,
            adLayout = LayoutInfo.LayoutByName(adLayout),
            adKey = adKey,
            shimmerInfo = showShimmerLayout,
            oneTimeUse = showNewAdEveryTime,
            requestNewOnShow = requestNewOnShow,
            listener = listener
        )
    }
}

fun AdsUiWidget.sdkBannerAd(
    activity: Activity,
    adKey: String,
    placementKey: String,
    lifecycle: Lifecycle,
    type: BannerAdType = BannerAdType.Normal(BannerAdSize.AdaptiveBanner),
    showShimmerLayout: ShimmerInfo = ShimmerInfo.GivenLayout(),
    requestNewOnShow: Boolean = false,
    showNewAdEveryTime: Boolean = true,
    defaultEnable: Boolean = true,
    listener: UiAdsListener? = null
) {
    apply {
        attachWithLifecycle(lifecycle = lifecycle, true)
        setWidgetKey(
            placementKey = placementKey, adKey = adKey,
            model = null,
            defEnabled = defaultEnable
        )
        showBannerAdmob(
            activity = activity,
            bannerAdType = type,
            adKey = adKey,
            shimmerInfo = showShimmerLayout,
            oneTimeUse = showNewAdEveryTime,
            requestNewOnShow = requestNewOnShow,
            listener = listener
        )
    }
}
