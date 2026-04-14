package com.example.workmanager.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.workmanager.CHANNEL_ID
import com.example.workmanager.NOTIFICATION_ID
import com.example.workmanager.NOTIFICATION_TITLE
import com.example.workmanager.OUTPUT_PATH
import com.example.workmanager.R
import com.example.workmanager.VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION
import com.example.workmanager.VERBOSE_NOTIFICATION_CHANNEL_NAME
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.UUID

/**
 * 1. Hàm hiển thị thông báo (Notification)
 */
fun makeStatusNotification(message: String, context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = VERBOSE_NOTIFICATION_CHANNEL_NAME
        val description = VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID, name, importance)
        channel.description = description

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        notificationManager?.createNotificationChannel(channel)
    }

    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle(NOTIFICATION_TITLE)
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setVibrate(LongArray(0))

    // Lưu ý: Nếu dòng dưới báo đỏ, Thi nhấn Alt+Enter để thêm quyền POST_NOTIFICATIONS nếu dùng Android 13+
    NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build())
}

/**
 * 2. Hàm tạm dừng luồng (Sleep) để dễ quan sát
 */
fun sleep() {
    try {
        Thread.sleep(3000, 0)
    } catch (e: InterruptedException) {
        // Ignore
    }
}

/**
 * 3. Hàm thực hiện thuật toán làm mờ ảnh
 */
fun blurBitmap(bitmap: Bitmap, applicationContext: Context): Bitmap {
    lateinit var rsContext: RenderScript
    try {
        val output = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config!!)
        rsContext = RenderScript.create(applicationContext)
        val inAlloc = Allocation.createFromBitmap(rsContext, bitmap)
        val outAlloc = Allocation.createFromBitmap(rsContext, output)
        val blurStrategy = ScriptIntrinsicBlur.create(rsContext, Element.U8_4(rsContext))

        blurStrategy.setRadius(10f)
        blurStrategy.setInput(inAlloc)
        blurStrategy.forEach(outAlloc)
        outAlloc.copyTo(output)

        return output
    } finally {
        rsContext.finish()
    }
}

/**
 * 4. Hàm lưu Bitmap vào file tạm và trả về Uri
 */
@Throws(IOException::class)
fun writeBitmapToFile(applicationContext: Context, bitmap: Bitmap): Uri {
    val name = String.format("blur-filter-output-%s.png", UUID.randomUUID().toString())
    val outputDir = File(applicationContext.filesDir, OUTPUT_PATH)
    if (!outputDir.exists()) {
        outputDir.mkdirs()
    }
    val outputFile = File(outputDir, name)
    var out: FileOutputStream? = null
    try {
        out = FileOutputStream(outputFile)
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, out)
    } finally {
        out?.let {
            try {
                it.close()
            } catch (ignore: IOException) {
            }
        }
    }
    return Uri.fromFile(outputFile)
}