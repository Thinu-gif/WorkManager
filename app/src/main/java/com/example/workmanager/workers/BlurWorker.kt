package com.example.workmanager.workers

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.workmanager.KEY_IMAGE_URI
import com.example.workmanager.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val TAG = "BlurWorker"

class BlurWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {

    @SuppressLint("MissingPermission")
    override suspend fun doWork(): Result {
        val appContext = applicationContext

        val blurLevel = inputData.getInt("KEY_BLUR_LEVEL", 1)

        makeStatusNotification("Đang làm mờ ảnh mức độ $blurLevel nè Thi...", appContext)

        sleep()

        return withContext(Dispatchers.IO) {
            return@withContext try {

                var picture = BitmapFactory.decodeResource(
                    appContext.resources,
                    R.drawable.android_cupcake
                )

                repeat(blurLevel) {
                    picture = blurBitmap(picture, appContext)
                }

                val outputUri = writeBitmapToFile(appContext, picture)

                val outputData = workDataOf(KEY_IMAGE_URI to outputUri.toString())

                makeStatusNotification("Làm mờ mức độ $blurLevel xong rồi đó!", appContext)

                Result.success(outputData)

            } catch (throwable: Throwable) {
                Log.e(TAG, "Lỗi khi làm mờ rồi Thi ơi: ", throwable)
                Result.failure()
            }
        }
    }
}