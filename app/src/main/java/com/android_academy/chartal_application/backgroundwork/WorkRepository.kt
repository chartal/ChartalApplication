package com.android_academy.chartal_application.backgroundwork

import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import java.util.concurrent.TimeUnit

class WorkRepository {

    private val constraintsNetwork = Constraints.Builder().setRequiredNetworkType(NetworkType.UNMETERED).build()
    private  val constraintsCharging = Constraints.Builder().setRequiresCharging(true).build()
    var myWorkRequest = PeriodicWorkRequest.Builder(UpdateCacheWorker::class.java, 8, TimeUnit.HOURS)
        .setConstraints(constraintsNetwork)
        .setConstraints(constraintsCharging)
        .build()
}
