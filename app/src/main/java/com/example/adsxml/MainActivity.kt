package com.example.adsxml

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.adsxml.databinding.ActivityMainBinding
import com.monetization.adsmain.commons.addNewController
import com.monetization.adsmain.commons.loadAd
import com.monetization.adsmain.commons.sdkBannerAd
import com.monetization.adsmain.commons.sdkNativeAd
import com.monetization.adsmain.splash.AdmobSplashAdController
import com.monetization.bannerads.AdmobBannerAdsManager
import com.monetization.bannerads.BannerAdSize
import com.monetization.bannerads.BannerAdType
import com.monetization.consent.ConsentListener
import com.monetization.consent.GoogleConsent
import com.monetization.core.ad_units.core.AdType
import com.monetization.core.commons.NativeTemplates
import com.monetization.core.listeners.UiAdsListener
import com.monetization.core.utils.dialog.SdkDialogs
import com.monetization.interstitials.AdmobInterstitialAdsManager
import com.monetization.nativeads.AdmobNativeAdsManager
import com.remote.firebaseconfigs.RemoteCommons.toConfigString
import com.remote.firebaseconfigs.SdkConfigListener
import com.remote.firebaseconfigs.SdkRemoteConfigController
import org.koin.android.ext.android.inject

private var TestEnabled = false

class MainActivity : ComponentActivity() {
    private lateinit var binding: ActivityMainBinding

    private val splashAdController: AdmobSplashAdController by inject()
    private val consent: GoogleConsent by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AdmobInterstitialAdsManager.addNewController(
            "Splash", listOf("")
        )
        AdmobInterstitialAdsManager.addNewController(
            "NewInter", listOf("", "", "", "")
        )
        AdmobNativeAdsManager.addNewController(
            "Native", listOf("", "", "", "")
        )
        AdmobBannerAdsManager.addNewController(
            "Banner", listOf("", "", "", "")
        )
        val dialog = SdkDialogs(this)

        binding.fetchConfig.setOnClickListener {
            SdkRemoteConfigController.fetchRemoteConfig(
                defaultXml = R.xml.backup_rules,
                callback = object : SdkConfigListener {
                    override fun onDismiss() {

                    }

                    override fun onFailure(error: String) {

                    }

                    override fun onSuccess() {
                        assignConfigs()
                    }

                    override fun onUpdate() {
                    }
                },
                onUpdate = {
                    assignConfigs()
                }
            )
        }
        binding.preloadAd.setOnClickListener {
            "Native".loadAd(AdType.INTERSTITIAL, this@MainActivity)
        }
        binding.showAd.setOnClickListener {
            showBannerAd()
//            showNativeAd()
        }

    }

    private fun assignConfigs() {
        TestEnabled = SdkRemoteConfigController.getRemoteConfigBoolean("TestEnabled")
        Toast.makeText(this, "TestEnabled=$TestEnabled", Toast.LENGTH_SHORT).show()
    }

    private fun showNativeAd() {
        binding.adFrame.sdkNativeAd(
            activity = this,
            adLayout = NativeTemplates.SmallNative,
            adKey = "Native",
            placementKey = true.toConfigString(),
            showNewAdEveryTime = true,
            lifecycle = lifecycle,
            listener = object : UiAdsListener {
                override fun onAdClicked(key: String) {
                    super.onAdClicked(key)
                }
            }
        )
    }

    private fun showBannerAd() {
        binding.adFrame.sdkBannerAd(
            activity = this,
            type = BannerAdType.Normal(BannerAdSize.Banner),
            adKey = "Banner",
            placementKey = true.toConfigString(),
            showNewAdEveryTime = true,
            lifecycle = lifecycle,
            listener = object : UiAdsListener {
                override fun onAdClicked(key: String) {
                    super.onAdClicked(key)
                }
            }
        )
    }
}