package com.example.workmanager.data

import android.content.Context
import android.net.Uri
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.workmanager.IMAGE_MANIPULATION_WORK_NAME
import com.example.workmanager.KEY_IMAGE_URI
import com.example.workmanager.TAG_OUTPUT
import com.example.workmanager.workers.BlurWorker
import com.example.workmanager.workers.CleanupWorker
import com.example.workmanager.workers.SaveImageToFileWorker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull

class WorkManagerBluromaticRepository(context: Context) : BluromaticRepository {

    private val workManager = WorkManager.getInstance(context)

    override val outputWorkInfo: Flow<WorkInfo?> =
        workManager.getWorkInfosByTagFlow(TAG_OUTPUT).mapNotNull { it.firstOrNull() }

    override fun applyBlur(blurLevel: Int) {
        var continuation = workManager
            .beginUniqueWork(
                IMAGE_MANIPULATION_WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                OneTimeWorkRequest.from(CleanupWorker::class.java)
            )

        val blurInputData = Data.Builder()
            .putInt("KEY_BLUR_LEVEL", blurLevel)

            .build()

        val blurRequest = OneTimeWorkRequestBuilder<BlurWorker>()
            .addTag(TAG_OUTPUT)
            .setInputData(blurInputData)
            .build()

        continuation = continuation.then(blurRequest)

        val saveRequest = OneTimeWorkRequestBuilder<SaveImageToFileWorker>()
            .addTag(TAG_OUTPUT)
            .build()

        continuation = continuation.then(saveRequest)

        continuation.enqueue()
    }
}