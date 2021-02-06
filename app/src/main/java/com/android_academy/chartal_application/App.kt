package com.android_academy.chartal_application

import android.app.Application
import androidx.work.WorkManager
import com.android_academy.chartal_application.backgroundwork.WorkRepository

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        WorkManager.getInstance(instance).enqueue(WorkRepository().myWorkRequest)
    }

    companion object {
        lateinit var instance: App
    }
}