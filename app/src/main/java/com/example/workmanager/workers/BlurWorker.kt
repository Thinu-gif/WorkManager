package com.example.workmanager.workers

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

    override suspend fun doWork(): Result {
        val appContext = applicationContext

        // 1. Lấy mức độ làm mờ từ dữ liệu đầu vào (mặc định là 1 nếu không tìm thấy)
        val blurLevel = inputData.getInt("KEY_BLUR_LEVEL", 1)

        // Hiển thị thông báo bắt đầu
        makeStatusNotification("Đang làm mờ ảnh mức độ $blurLevel nè Thi...", appContext)

        // Giả lập xử lý nặng
        sleep()

        return withContext(Dispatchers.IO) {
            return@withContext try {
                // 2. Lấy ảnh gốc từ drawable
                // Lưu ý: Nếu Thi dùng ảnh mèo thì thay R.drawable.android_cupcake bằng tên ảnh mèo của Thi nhé
                var picture = BitmapFactory.decodeResource(
                    appContext.resources,
                    R.drawable.android_cupcake
                )

                // 3. Thực hiện lặp lại việc làm mờ dựa trên mức độ Thi chọn
                repeat(blurLevel) {
                    picture = blurBitmap(picture, appContext)
                }

                // 4. Lưu kết quả cuối cùng vào file tạm
                val outputUri = writeBitmapToFile(appContext, picture)

                // 5. Trả về URI của ảnh đã mờ để Worker tiếp theo (Save) sử dụng
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