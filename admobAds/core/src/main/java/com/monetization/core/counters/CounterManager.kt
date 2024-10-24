package com.monetization.core.counters

import android.util.Log

object CounterManager {

    private val counters = HashMap<String, CounterInfo>()

    fun createACounter(key: String, info: CounterInfo) {
        counters[key] = info
    }

    fun adShownCounterReact(counterKey: String, adShown: Boolean) {
        val model = counterKey.getCounterModel()
        val strategy = if (adShown) {
            model.adShownStrategy
        } else {
            model.adNotShownStrategy
        }
        when (strategy) {
            CounterStrategies.KeepSameValue -> {
            }

            CounterStrategies.ResetToZero -> {
                counterKey.changeCurrentCounter(0)
            }

            is CounterStrategies.SetStartingTo -> {
                counterKey.changeCurrentCounter(strategy.startPoint)
            }
        }
    }

    fun counterWrapper(
        key: String,
        onDismiss: (Boolean) -> Unit,
        showAd: () -> Unit
    ) {
        val model = key.getCounterModel()
        val counterReached = model.isCounterReached()
        if (counterReached) {
            logCounterDetails("Counter Reached")
            showAd.invoke()
        } else {
            key.incrementCounter()
            logCounterDetails("Counter Progress: Current=${key.getCounterModel().currentPoint},Target=${model.maxPoint}")
            onDismiss.invoke(false)
        }
    }

    fun String.isCounterReached(): Boolean {
        return getCounterModel().isCounterReached()
    }

    fun String.incrementCounter() {
        val model = getCounterModel()
        counters[this] = model.copy(
            currentPoint = model.currentPoint + 1
        )
    }

    fun String.decrementCounter() {
        val model = getCounterModel()
        counters[this] = model.copy(
            currentPoint = model.currentPoint - 1
        )
    }

    fun String.changeMaxCounter(maxCounter: Int) {
        val model = getCounterModel().copy(
            maxPoint = maxCounter
        )
        counters[this] = model
    }

    fun String.changeCurrentCounter(counter: Int) {
        val model = getCounterModel().copy(
            currentPoint = counter
        )
        counters[this] = model
    }

    fun String.getCounterModel(): CounterInfo {
        val model =
            counters[this] ?: throw IllegalArgumentException("No Counter found against key : $this")
        return model
    }

    fun logCounterDetails(msg: String, error: Boolean = false) {
        if (error) {
            Log.e("CounterLogs", "CounterLogs:$msg")
        } else {
            Log.d("CounterLogs", "CounterLogs:$msg")
        }
    }

}