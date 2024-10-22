package com.monetization.core.ui

import android.view.View

sealed class ShimmerInfo(
    open val hideShimmerOnFailure: Boolean = true,
) {

    data object None : ShimmerInfo()
    data class GivenLayout(
        override val hideShimmerOnFailure: Boolean = true
    ) : ShimmerInfo()

    data class ShimmerByView(
        val layoutView: View?,
        override val hideShimmerOnFailure: Boolean = true,
    ) : ShimmerInfo()
}