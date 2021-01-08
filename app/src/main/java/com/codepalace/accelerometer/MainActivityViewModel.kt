package com.codepalace.accelerometer

import androidx.lifecycle.ViewModel

class MainActivityViewModel: ViewModel() {

    var isALreadySet = false
    var xAxis = 0f

    fun updateIsALreadySet(value: Boolean) {
        isALreadySet = value
    }

    fun updateXAxis(value: Float) {
        xAxis = value
    }
}