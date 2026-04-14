package com.example.workmanager.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.workmanager.R

@Composable
fun BluromaticScreen(blurViewModel: BlurViewModel) {
    val blurLevel by blurViewModel.blurLevel.collectAsState()
    val workInfo by blurViewModel.outputWorkInfo.collectAsState()
    val outputUri = blurViewModel.getOutputUri(workInfo)

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(outputUri ?: R.drawable.android_cupcake)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.android_cupcake),
            error = painterResource(R.drawable.android_cupcake),
            contentDescription = "Ảnh làm mờ",
            modifier = Modifier.size(300.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))
        Text(text = "Chọn mức độ làm mờ cho Thi:", style = MaterialTheme.typography.titleLarge)

        listOf(1, 2, 3).forEach { level ->
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {
                RadioButton(
                    selected = (blurLevel == level),
                    onClick = { blurViewModel.setBlurLevel(level) }
                )
                Text(text = "Mức độ $level", modifier = Modifier.padding(start = 8.dp))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        val isWorking = workInfo?.state?.isFinished == false && workInfo != null
        Button(
            onClick = { blurViewModel.applyBlur(blurLevel) },
            enabled = !isWorking
        ) {
            if (isWorking) {
                CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                Spacer(Modifier.width(8.dp))
                Text("Đang xử lý...")
            } else {
                Text("Bắt đầu làm mờ ảnh (Start)")
            }
        }
    }
}