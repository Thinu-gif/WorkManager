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

    // Theo dõi trạng thái của các công việc có gắn tag là TAG_OUTPUT
    // Chúng ta lấy WorkInfo đầu tiên tìm thấy được gắn nhãn này
    override val outputWorkInfo: Flow<WorkInfo?> =
        workManager.getWorkInfosByTagFlow(TAG_OUTPUT).mapNotNull { it.firstOrNull() }

    override fun applyBlur(blurLevel: Int) {
        // 1. Tạo chuỗi công việc duy nhất để tránh chạy chồng chéo
        var continuation = workManager
            .beginUniqueWork(
                IMAGE_MANIPULATION_WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                OneTimeWorkRequest.from(CleanupWorker::class.java)
            )

        // 2. Tạo gói dữ liệu (Input Data) chứa mức độ mờ
        val blurInputData = Data.Builder()
            .putInt("KEY_BLUR_LEVEL", blurLevel) // Gửi mức độ mờ (1, 2, 3)
            // Lưu ý: Nếu Thi muốn làm mờ ảnh bất kỳ, hãy truyền URI ảnh vào đây:
            // .putString(KEY_IMAGE_URI, imageUri.toString())
            .build()

        // 3. Tạo yêu cầu làm mờ (BlurWorker)
        val blurRequest = OneTimeWorkRequestBuilder<BlurWorker>()
            .addTag(TAG_OUTPUT)
            .setInputData(blurInputData) // Đưa gói dữ liệu vào đây
            .build()

        continuation = continuation.then(blurRequest)

        // 4. Tạo yêu cầu lưu file (SaveImageToFileWorker)
        val saveRequest = OneTimeWorkRequestBuilder<SaveImageToFileWorker>()
            .addTag(TAG_OUTPUT)
            .build()

        continuation = continuation.then(saveRequest)

        // 5. Đẩy vào hàng chờ thực thi
        continuation.enqueue()
    }
}