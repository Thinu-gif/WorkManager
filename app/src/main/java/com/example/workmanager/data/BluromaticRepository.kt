package com.example.workmanager.data

import kotlinx.coroutines.flow.Flow
import androidx.work.WorkInfo

interface BluromaticRepository {
    // Hàm để ra lệnh bắt đầu làm mờ ảnh
    fun applyBlur(blurLevel: Int)

    // Hàm để theo dõi trạng thái công việc (đang chạy, đã xong, hay lỗi)
    val outputWorkInfo: Flow<WorkInfo?>
}