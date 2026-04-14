package com.example.workmanager.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.workmanager.OUTPUT_PATH
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

private const val TAG = "CleanupWorker"

class CleanupWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {

    override suspend fun doWork(): Result {
        // Hiện thông báo để Thi biết app đang dọn dẹp tệp cũ
        makeStatusNotification("Đang dọn dẹp các tệp tạm...", applicationContext)

        // Ngủ 3 giây để Thi kịp nhìn thấy thông báo trên thanh Status
        sleep()

        return withContext(Dispatchers.IO) {
            return@withContext try {
                // Đường dẫn đến thư mục chứa ảnh làm mờ tạm thời
                val outputDirectory = File(applicationContext.filesDir, OUTPUT_PATH)

                if (outputDirectory.exists()) {
                    val entries = outputDirectory.listFiles()
                    if (entries != null) {
                        for (entry in entries) {
                            val name = entry.name
                            // Kiểm tra nếu là file ảnh (.png) thì xóa đi
                            if (name.isNotEmpty() && name.endsWith(".png")) {
                                val deleted = entry.delete()
                                Log.i(TAG, "Đã xóa $name: $deleted")
                            }
                        }
                    }
                }
                Result.success()
            } catch (exception: Exception) {
                Log.e(TAG, "Lỗi khi dọn dẹp tệp: ", exception)
                Result.failure()
            }
        }
    }
}