package com.yaman.kotlin_demo.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class Work(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {

        try {
            getCurrentLocations()
        } catch (e: Exception) {
            return Result.retry()
        }
        return Result.success()
    }

    private fun getCurrentLocations() {

    }

}