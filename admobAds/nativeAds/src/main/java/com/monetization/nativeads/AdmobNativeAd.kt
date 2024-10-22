package com.monetization.nativeads

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.monetization.core.ad_units.GeneralNativeAd
import com.monetization.core.ad_units.core.AdType
import com.monetization.core.commons.AdsCommons
import com.monetization.core.commons.AdsCommons.logAds
import com.monetization.core.commons.NativeConstants.makeGone
import com.monetization.core.commons.Utils
import com.monetization.core.ui.AdsWidgetData
import com.monetization.nativeads.ui.AdmobNativeMediaView
import com.monetization.nativeads.ui.AdmobNativeView

class AdmobNativeAd(
    val adKey: String,
    val nativeAd: NativeAd,
) : GeneralNativeAd {
    override fun getTitle(): String? {
        return nativeAd.headline
    }

    override fun getDescription(): String? {
        return nativeAd.body
    }

    override fun getCtaText(): String? {
        return nativeAd.callToAction
    }

    override fun getAdvertiserName(): String? {
        return nativeAd.advertiser
    }

    override fun destroyAd(activity: Activity) {
        AdmobNativeAdsManager.getAdController(adKey)?.destroyAd(activity)
    }

    /*
        fun populateUnifiedNativeAdViewLarge(nativeAd: NativeAd, adView: NativeAdView) {

            adView.mediaView = adView.findViewById(R.id.ad_media)
            adView.mediaView?.let { adMedia ->
                if (nativeAd.mediaContent == null) {
                    adMedia.visibility = View.GONE
                } else {
                    adMedia.visibility = View.VISIBLE
                    adMedia.setOnHierarchyChangeListener(object : ViewGroup.OnHierarchyChangeListener {
                        override fun onChildViewAdded(parent: View?, child: View?) {
                            try {
                                if (child !is ImageView) {
                                    return
                                }
                                child.adjustViewBounds = true
                                child.scaleType = ImageView.ScaleType.CENTER_CROP
                            } catch (_: Exception) {
                            }
                        }

                        override fun onChildViewRemoved(parent: View?, child: View?) {
                        }
                    })
                }
            }
            val button = adView.findViewById<Button>(R.id.ad_call_to_action)
            adView.headlineView = adView.findViewById(R.id.ad_headline)
            adView.bodyView = adView.findViewById(R.id.ad_body)
            adView.iconView = adView.findViewById(R.id.ad_app_icon)
            adView.callToActionView = button

            if (nativeAd.headline == null) {
                adView.headlineView?.visibility = View.GONE
            } else {
                adView.headlineView?.apply {
                    this.visibility = View.VISIBLE
                    (this as TextView).text = nativeAd.headline
                }
            }
            if (nativeAd.callToAction == null) {
                adView.callToActionView?.visibility = View.GONE
            } else {
                adView.callToActionView?.apply {
                    this.visibility = View.VISIBLE
                    (this as Button).text = nativeAd.callToAction
                }
            }
            if (nativeAd.body == null) {
                adView.bodyView?.visibility = View.GONE
            } else {
                adView.bodyView?.apply {
                    this.visibility = View.VISIBLE
                    (this as TextView).text = nativeAd.body
                }
            }
            adView.iconView?.let {
                if (nativeAd.icon == null) {
                    it.visibility = View.GONE
                } else {
                    (it as ImageView).setImageDrawable(nativeAd.icon!!.drawable)
                    it.visibility = View.VISIBLE
                }
            }
            adView.setNativeAd(nativeAd)
        }*/
    override fun populateAd(
        activity: Activity,
        adViewLayout: View?,
        adsWidgetData: AdsWidgetData?,
        onPopulated: () -> Unit,
    ) {

        val ad = this
        if (adViewLayout is AdmobNativeView) {
            logAds(
                "populateNativeAd Called NativeAdsManager " +
                        ",isNativeAd=${ad},NativeAdView=${adViewLayout.getNativeAdView() != null}"
            )
            (ad as? AdmobNativeAd)?.nativeAd?.let { nativeAd ->
                adViewLayout.apply {
                    getNativeAdView()?.let { nativeAdView ->
                        val adHeadLine: TextView? = findViewById(R.id.ad_headline)
                        val adBody: TextView? = findViewById(R.id.ad_body)
                        val mediaView: AdmobNativeMediaView? = findViewById(R.id.ad_media)
                        val adCtaBtn: TextView? = findViewById(R.id.ad_call_to_action)
                        val addAttrTextView: TextView? = findViewById(R.id.addAttr)
                        val mIconView = nativeAdView.findViewById<ImageView>(R.id.ad_app_icon)
                        val mMedia = mediaView?.getMediaView()
// Setting Up Ads Widget Data
                        setAdsWidgetData(
                            context = activity,
                            adsWidgetData = adsWidgetData,
                            adHeadLine = adHeadLine,
                            adBody = adBody,
                            adCtaBtn = adCtaBtn,
                            mIconView = mIconView,
                            attrTextView = addAttrTextView,
                            mediaView = mediaView
                        )
//
                        AdsCommons.logAds(
                            "populateAd isMediaViewOk=${mMedia != null}",
                            isError = mMedia == null
                        )
                        mediaView?.let {
                            nativeAdView.mediaView = mMedia
                            try {
                                nativeAdView.mediaView?.let { adMedia ->
                                    adMedia.makeGone(nativeAd.mediaContent == null)
                                    mMedia.makeGone(nativeAd.mediaContent == null)
                                    if (nativeAd.mediaContent != null) {
                                        adMedia.mediaContent = nativeAd.mediaContent
                                    }
                                } ?: run {
                                    nativeAdView.mediaView?.makeGone()
                                    mMedia?.makeGone()
                                }
                            } catch (_: Exception) {
                                nativeAdView.mediaView?.makeGone()
                                mMedia?.makeGone()
                            }
                        }
                        nativeAdView.iconView = mIconView
                        nativeAdView.iconView?.let {
                            nativeAd.icon.let { icon ->
                                nativeAdView.mediaView?.makeGone(icon == null)
                                if (icon != null) {
                                    (it as? ImageView)?.setImageDrawable(icon.drawable)
                                }
                            }
                        } ?: run {
                            mIconView.makeGone()
                        }

                        nativeAdView.callToActionView = adCtaBtn
                        nativeAdView.bodyView = adBody
                        nativeAdView.headlineView = adHeadLine

                        if (nativeAd.headline.isNullOrEmpty()) {
                            adHeadLine?.visibility = View.GONE
                        } else {
                            adHeadLine?.visibility = View.VISIBLE
                            adHeadLine?.text = nativeAd.headline
                        }

                        if (nativeAd.body.isNullOrEmpty()) {
                            adBody?.visibility = View.GONE
                        } else {
                            adBody?.visibility = View.VISIBLE
                            adBody?.text = nativeAd.body
                        }
                        nativeAd.callToAction?.let { btn ->
                            adCtaBtn?.text = btn
                        }
                        nativeAdView.setNativeAd(nativeAd)
                        onPopulated.invoke()
                    }
                }
            }
        }
    }

    private fun setAdsWidgetData(
        context: Context,
        adsWidgetData: AdsWidgetData?,
        adHeadLine: TextView?,
        adBody: TextView?,
        adCtaBtn: TextView?,
        mediaView: AdmobNativeMediaView?,
        mIconView: ImageView?,
        attrTextView: TextView?,
    ) {
        logAds("setAdsWidgetData=$adsWidgetData")
        adsWidgetData?.let { data ->
//          Cta Button
            adCtaBtn?.let { cta ->
                data.adCtaBgColor?.let {
                    Utils.setColorFilterByColor(
                        cta.background,
                        Color.parseColor(it)
                    )
                }
                setTextColor(cta, data.adCtaTextColor)
            }
            attrTextView?.let {
                data.adAttrBgColor?.let { attrBg ->
                    Utils.setColorFilterByColor(it.background, Color.parseColor(attrBg))
                }
            }

            val margins = adsWidgetData.margings ?: ""
            val marginList = margins.split(",")
            if (margins.isNotBlank() && marginList.size == 4) {
                val layoutParams = mediaView?.layoutParams as? ViewGroup.MarginLayoutParams
//                layoutParams?.setMargins(leftMargin, topMargin, rightMargin, bottomMargin)
                layoutParams?.setMargins(
                    marginList[0].toIntOrZero(), // Left
                    marginList[1].toIntOrZero(), // Top
                    marginList[2].toIntOrZero(), // Right
                    marginList[3].toIntOrZero() // Bottom
                )
                mediaView?.layoutParams = layoutParams
            }



            setTextColor(adHeadLine, data.adHeadLineTextColor)
            setTextColor(adBody, data.adBodyTextColor)
            setTextColor(attrTextView, data.adAttrTextColor)
            setHeightWidthOfView(adCtaBtn, context, data.adCtaHeight)
            setHeightWidthOfView(mediaView, context, data.adMediaViewHeight)
            setHeightWidthOfView(mIconView, context, data.adIconHeight, data.adIconWidth)
            setTextSize(adHeadLine, data.adHeadlineTextSize)
            setTextSize(adBody, data.adBodyTextSize)
            setTextSize(adCtaBtn, data.adCtaTextSize)
        }
    }

    private fun setHeightWidthOfView(
        view: View?,
        context: Context,
        height: Float?,
        width: Float? = null,
    ) {
        view?.let {
            val layoutParams = view.layoutParams
            height?.let {
                layoutParams.height = dpToPx(context, height).toInt()
            }
            width?.let {
                layoutParams.width = dpToPx(context, width).toInt()
            }
            view.layoutParams = layoutParams
        }
    }

    private fun dpToPx(context: Context, dp: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        )
    }

    private fun setTextSize(view: TextView?, textSize: Float? = null) {
        try {
            if (view is TextView) {
                textSize?.let {
                    view.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize)
                }
            }
        } catch (_: Exception) {
            logAds("Exception while setting text size on native", true)
        }
    }

    private fun setTextColor(view: TextView?, color: String? = null) {
        try {
            view?.let {
                color?.let {
                    view.setTextColor(Color.parseColor(it))
                }
            }
        } catch (_: Exception) {
            logAds("Exception while setting text color on native", true)
        }
    }


    override fun getAdType(): AdType {
        return AdType.NATIVE
    }

    private fun String.toIntOrZero(value: Int = 0): Int {
        return toIntOrNull() ?: value
    }
}