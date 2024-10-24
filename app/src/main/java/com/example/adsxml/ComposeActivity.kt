package com.example.adsxml

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import com.example.adsxml.databinding.ActivityMainBinding
import com.monetization.adsmain.commons.sdkNativeAd
import com.monetization.core.commons.NativeTemplates
import com.monetization.core.listeners.UiAdsListener
import com.remote.firebaseconfigs.RemoteCommons.toConfigString

class ComposeActivity : ComponentActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showNativeAd()
        binding.showAd.setOnClickListener {
            binding.adFrameTwo.refreshAd(isNativeAd = true)
//            showNativeAdTwo()
        }
    }

    private fun showNativeAd() {
//        Handler(Looper.getMainLooper()).postDelayed({
        binding.adFrameTwo.sdkNativeAd(
            activity = this,
            adLayout = NativeTemplates.LargeNative,
            adKey = "Native",
            placementKey = "SDK_TRUE",
            showNewAdEveryTime = true,
            lifecycle = lifecycle,
            listener = object : UiAdsListener {
                override fun onAdClicked(key: String) {
                    super.onAdClicked(key)
                }
            }
        )
//        }, 10)
    }

    private fun showNativeAdTwo() {
        Handler(Looper.getMainLooper()).postDelayed({
            binding.adFrame.sdkNativeAd(
                activity = this,
                adLayout = NativeTemplates.LargeNative,
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
        }, 10)
    }
}