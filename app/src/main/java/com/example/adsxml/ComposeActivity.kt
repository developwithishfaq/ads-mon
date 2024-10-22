package com.example.adsxml

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.adsxml.ads.AdsEntryManager

class ComposeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AdsEntryManager.initAds(this)

        setContent {
//            val sdkNativeViewModel: SdkNativeViewModel = koinViewModel()
            var showDialog by rememberSaveable {
                mutableStateOf(false)
            }/*
            if (showDialog) {
                Dialog(onDismissRequest = {
                    sdkNativeViewModel.destroyAdByKey("Test")
                    showDialog = false
                }) {
                    Surface {

                    }
                }
            }*/
            /*Column {
                SdkNativeAd(
                    activity = this@ComposeActivity,
                    adLayout = NativeTemplates.LargeNative,
                    adKey = "Main",
                    placementKey = true.toConfigString(),
                    showNewAdEveryTime = true,
                    sdkNativeViewModel = sdkNativeViewModel,
                    showShimmerLayout = com.monetization.nativeads.R.layout.large_native_ad_shimmer.toShimmerView(
                        this@ComposeActivity
                    )
                )
                Button(onClick = {
//                    AdType.INTERSTITIAL.loadAd("Main", this@ComposeActivity)
                    showDialog = true
                }) {

                }
            }*/
        }
    }
}