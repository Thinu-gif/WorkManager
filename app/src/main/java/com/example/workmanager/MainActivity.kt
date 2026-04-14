package com.example.workmanager

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.workmanager.ui.BlurViewModel
import com.example.workmanager.ui.BluromaticScreen
import com.example.workmanager.ui.theme.WorkManagerTheme

class MainActivity : ComponentActivity() {

    // Đoạn code này để xử lý bảng hỏi xin quyền
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        // Thi có thể log ra để xem người dùng có cho phép hay không
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Xin quyền thông báo ngay khi mở app (chỉ dành cho Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        setContent {
            WorkManagerTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val viewModel: BlurViewModel = viewModel(factory = BlurViewModel.Factory)
                    BluromaticScreen(blurViewModel = viewModel)
                }
            }
        }
    }
}