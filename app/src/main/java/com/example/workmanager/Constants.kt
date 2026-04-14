package com.example.workmanager

/**
 * Các hằng số dùng cho Thông báo (Notification)
 */
// Tên của kênh thông báo hiển thị trong cài đặt hệ thống
val VERBOSE_NOTIFICATION_CHANNEL_NAME: CharSequence = "Thông báo WorkManager"
const val VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION = "Hiển thị thông báo khi các worker bắt đầu chạy"
val NOTIFICATION_TITLE: CharSequence = "Đang xử lý ảnh..."
const val CHANNEL_ID = "VERBOSE_NOTIFICATION"
const val NOTIFICATION_ID = 1

/**
 * Các hằng số dùng cho WorkManager
 */
// Tên định danh cho chuỗi công việc xử lý ảnh
const val IMAGE_MANIPULATION_WORK_NAME = "image_manipulation_work"

// Tên thư mục tạm để lưu ảnh sau khi làm mờ
const val OUTPUT_PATH = "blur_filter_outputs"

// Khóa (Key) dùng để truyền và nhận dữ liệu URI của ảnh giữa các Worker
const val KEY_IMAGE_URI = "KEY_IMAGE_URI"

// Thẻ (Tag) dùng để truy vấn trạng thái công việc từ UI
const val TAG_OUTPUT = "OUTPUT"

// Thời gian chờ giả lập (3 giây) để Thi kịp nhìn thấy thông báo trên thanh trạng thái
const val DELAY_TIME_MILLIS: Long = 3000