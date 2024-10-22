package com.monetization.nativeads.ui

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.facebook.shimmer.ShimmerFrameLayout
import com.monetization.core.ad_units.core.AdType
import com.monetization.core.commons.AdsCommons.logAds
import com.monetization.core.commons.NativeConstants.inflateLayoutByLayoutInfo
import com.monetization.core.commons.NativeConstants.removeViewsFromIt
import com.monetization.core.listeners.UiAdsListener
import com.monetization.core.managers.AdsLoadingStatusListener
import com.monetization.core.ui.LayoutInfo
import com.monetization.core.ui.ShimmerInfo
import com.monetization.core.ui.widgetBase.BaseAdsWidget
import com.monetization.nativeads.AdmobNativeAd
import com.monetization.nativeads.AdmobNativeAdsController
import com.monetization.nativeads.AdmobNativeAdsManager
import com.monetization.nativeads.R

class NativeAdWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : BaseAdsWidget<AdmobNativeAdsController>(context, attrs, defStyleAttr) {

    init {
        logAds("NativeWidget called", true)
    }

    private var layoutView: LayoutInfo? = null


    fun showNativeAdmob(
        activity: Activity,
        adKey: String,
        adLayout: LayoutInfo,
        enabled: Boolean,
        shimmerInfo: ShimmerInfo = ShimmerInfo.GivenLayout(),
        oneTimeUse: Boolean = true,
        requestNewOnShow: Boolean = true,
        listener: UiAdsListener? = null
    ) {
        this.layoutView = if (isValuesFromRemote) {
            if (adsWidgetData?.adLayout != null) {
                LayoutInfo.LayoutByName(adsWidgetData!!.adLayout!!)
            } else {
                adLayout
            }
        } else {
            adLayout
        }
        onShowAdCalled(
            adKey = adKey,
            activity = activity,
            oneTimeUse = oneTimeUse,
            requestNewOnShow = requestNewOnShow,
            enabled = enabled,
            shimmerInfo = shimmerInfo,
            adsManager = AdmobNativeAdsManager,
            adType = AdType.NATIVE,
            listener = listener
        )
        logAds("showNativeAd called($key),enabled=$enabled,layoutView=$layoutView")
    }

    override fun loadAd() {
        (adsController as? AdmobNativeAdsController)?.loadAd(
            activity = (activity!!),
            calledFrom = "Base Native Activity",
            callback = getAdsLoadingListener()
        )
        /*object : AdsLoadingStatusListener {
            override fun onAdRequested(adKey: String) {
                uiListener?.onAdRequested(adKey)
            }

            override fun onImpression(adKey: String) {
                uiListener
            }

            override fun onAdLoaded(adKey: String) {
                uiListener?.onAdLoaded(adKey)
                if (adLoaded) {
                    return
                }
                adOnLoaded()
            }

            override fun onAdFailedToLoad(adKey: String, message: String, code: Int) {
                uiListener?.onAdFailed(adKey, message, code)
                adOnFailed()
            }
        }*/
    }

    override fun populateAd() {
        val layout = layoutView?.inflateLayoutByLayoutInfo(activity!!)
        val admobNativeView = layout?.findViewById<AdmobNativeView>(R.id.adView)
        logAds(
            "populateNativeAd(${activity?.localClassName?.substringAfterLast(".")}) " +
                    ",Ad Ok=${adUnit != null}," +
                    "Layout Ok=${layout != null},Native View Ok=${admobNativeView != null}"
        )
        adUnit?.let {
            addView(layout)
            admobNativeView?.let { view ->
                (it as AdmobNativeAd).populateAd(activity!!, view, adsWidgetData) {
                    if (oneTimeUse) {
                        adsController?.destroyAd(activity!!)
                        if (requestNewOnShow) {
                            adsController?.loadAd(activity!!, "requestNewOnShow", null)
                        }
                    }
                    refreshLayout()
                }
            }
        }
    }

    override fun showShimmerLayout() {
        val info = shimmerInfo
        val shimmerLayout = LayoutInflater.from(activity)
            .inflate(com.monetization.core.R.layout.shimmer, null, false)
            ?.findViewById<ShimmerFrameLayout>(com.monetization.core.R.id.shimmerRoot)
        val shimmerView: ShimmerFrameLayout? = when (info) {
            is ShimmerInfo.GivenLayout -> {
                val adLayout = layoutView?.inflateLayoutByLayoutInfo(activity!!)
                shimmerLayout?.removeViewsFromIt()
                shimmerLayout?.addView(adLayout)
                shimmerLayout
            }

            is ShimmerInfo.ShimmerByView -> {
                info.layoutView?.let {
                    shimmerLayout?.removeViewsFromIt()
                    shimmerLayout?.addView(it)
                    shimmerLayout
                }
            }

            ShimmerInfo.None -> {
                null
            }
        }
        removeAllViews()
        addView(shimmerView)
    }

}