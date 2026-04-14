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

const val IMAGE_MANIPULATION_WORK_NAME = "image_manipulation_work"

const val OUTPUT_PATH = "blur_filter_outputs"

const val KEY_IMAGE_URI = "KEY_IMAGE_URI"

const val TAG_OUTPUT = "OUTPUT"

const val DELAY_TIME_MILLIS: Long = 3000