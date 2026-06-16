package com.example.nhatky

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class QuoteWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val response = RetrofitClient.instance.getDailyQuote()
            if (response.isNotEmpty()) {
                val quote = response[0]
                showNotification(quote.q, quote.a)
            }
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }

    private fun showNotification(content: String, author: String) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "daily_quote_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Châm ngôn hàng ngày", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info) // Icon thông báo mặc định
            .setContentTitle("Châm ngôn chữa lành ngày mới")
            .setContentText("\"$content\" - $author")
            .setStyle(NotificationCompat.BigTextStyle().bigText("\"$content\" - $author"))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        notificationManager.notify(1, builder.build())
    }
}