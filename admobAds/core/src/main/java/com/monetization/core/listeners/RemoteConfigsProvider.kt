package com.monetization.core.listeners

import com.monetization.core.ui.AdsWidgetData

interface RemoteConfigsProvider {
    fun isAdEnabled(placementKey: String, adKey: String): Boolean
    fun getAdWidgetData(placementKey: String, adKey: String): AdsWidgetData?
}