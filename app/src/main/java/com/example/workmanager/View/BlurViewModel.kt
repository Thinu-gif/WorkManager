package com.example.workmanager.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.work.WorkInfo
import com.example.workmanager.KEY_IMAGE_URI
import com.example.workmanager.data.BluromaticRepository
import com.example.workmanager.data.WorkManagerBluromaticRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class BlurViewModel(private val bluromaticRepository: BluromaticRepository) : ViewModel() {

    // 1. Lưu trữ mức độ mờ
    private val _blurLevel = MutableStateFlow(1)
    val blurLevel: StateFlow<Int> = _blurLevel

    // 2. Quan sát trạng thái từ Repository
    val outputWorkInfo: StateFlow<WorkInfo?> = bluromaticRepository.outputWorkInfo
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    fun setBlurLevel(level: Int) {
        _blurLevel.value = level
    }

    fun applyBlur(blurLevel: Int) {
        bluromaticRepository.applyBlur(blurLevel)
    }

    // 3. Hàm lấy URI ảnh thành phẩm
    fun getOutputUri(workInfo: WorkInfo?): String? {
        return if (workInfo != null && workInfo.state.isFinished) {
            workInfo.outputData.getString(KEY_IMAGE_URI)
        } else {
            null
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val context = (this[APPLICATION_KEY] as Application).applicationContext
                BlurViewModel(bluromaticRepository = WorkManagerBluromaticRepository(context))
            }
        }
    }
}