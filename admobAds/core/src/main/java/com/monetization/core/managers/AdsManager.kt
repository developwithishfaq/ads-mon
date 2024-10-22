package com.monetization.core.managers

import com.monetization.core.controllers.AdsController
import com.monetization.core.controllers.AdsControllerBaseHelper

interface AdsManager<T : AdsControllerBaseHelper> {
    fun getAdController(key: String): AdsController?
    fun addNewController( controller: T)
    fun removeController(adKey: String)
    fun getAllController(): List<T>
}