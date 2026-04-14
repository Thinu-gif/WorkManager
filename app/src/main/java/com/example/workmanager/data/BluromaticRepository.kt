package com.example.workmanager.data

import kotlinx.coroutines.flow.Flow
import androidx.work.WorkInfo

interface BluromaticRepository {
    fun applyBlur(blurLevel: Int)

    val outputWorkInfo: Flow<WorkInfo?>
}