package com.example.workmanager.workers

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.workmanager.KEY_IMAGE_URI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val TAG = "SaveImageToFileWorker"

class SaveImageToFileWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {

    private val title = "Blurred Image"
    private val dateFormatter = SimpleDateFormat(
        "yyyy.MM.dd 'at' HH:mm:ss z",
        Locale.getDefault()
    )

    override suspend fun doWork(): Result {
        // Thông báo cho Thi biết là đang lưu ảnh vào máy

        makeStatusNotification("Đang lưu ảnh vào thư viện...", applicationContext)
        sleep()

        return withContext(Dispatchers.IO) {
            val resolver = applicationContext.contentResolver
            return@withContext try {
                // 1. Lấy URI của ảnh mờ từ dữ liệu đầu vào (Input Data)
                val resourceUri = inputData.getString(KEY_IMAGE_URI)

                // 2. Đọc ảnh từ URI đó thành Bitmap
                val bitmap = BitmapFactory.decodeStream(
                    resolver.openInputStream(Uri.parse(resourceUri))
                )

                // 3. Lưu Bitmap vào thư viện ảnh hệ thống
                val imageUrl = MediaStore.Images.Media.insertImage(
                    resolver, bitmap, title, dateFormatter.format(Date())
                )

                if (!imageUrl.isNullOrEmpty()) {
                    // 4. Nếu lưu thành công, gửi URI mới này đi để UI có thể dùng hiển thị
                    val output = workDataOf(KEY_IMAGE_URI to imageUrl)
                    makeStatusNotification("Ảnh đã được lưu thành công!", applicationContext)
                    Result.success(output)
                } else {
                    Log.e(TAG, "Không thể lưu ảnh vào MediaStore")
                    Result.failure()
                }
            } catch (exception: Exception) {
                Log.e(TAG, "Lỗi khi lưu ảnh: ", exception)
                Result.failure()
            }
        }
    }
}