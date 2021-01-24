package com.android_academy.chartal_application.util

import com.android_academy.chartal_application.data.Movie

interface INetworkStatus {
    fun internetConnectionStatus(): Boolean
}