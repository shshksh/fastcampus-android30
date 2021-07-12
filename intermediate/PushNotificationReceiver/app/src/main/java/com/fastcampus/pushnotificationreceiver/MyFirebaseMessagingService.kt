package com.fastcampus.pushnotificationreceiver

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }

    override fun onMessageReceived(removeMessage: RemoteMessage) {
        super.onMessageReceived(removeMessage)

        createNotificationChannel()

        val type = removeMessage.data["type"]?.let { NotificationType.valueOf(it) }
        val title = removeMessage.data["title"]
        val message = removeMessage.data["message"]

        type ?: return

        val notification = createNotification(type, title, message)

        notifyMessage(type.id, notification)
    }

    private fun notifyMessage(id: Int, notification: Notification) {
        NotificationManagerCompat.from(this)
            .notify(id, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = CHANNEL_DESCRIPTION

            (getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
                .createNotificationChannel(channel)
        }
    }

    private fun createNotification(
        type: NotificationType,
        title: String?,
        message: String?
    ): Notification {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("notificationType", "${type.title} 타입")
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        val pendingIntent = PendingIntent.getActivity(this, type.id, intent, FLAG_UPDATE_CURRENT)

        val notificationBuilder = createNotificationBuilder(title, message, pendingIntent)

        when (type) {
            NotificationType.NORMAL -> Unit
            NotificationType.EXPANDABLE -> setupExpandableNotification(notificationBuilder)
            NotificationType.CUSTOM -> setupCustomNotification(notificationBuilder, title, message)
        }
        return notificationBuilder.build()
    }

    private fun createNotificationBuilder(
        title: String?,
        message: String?,
        pendingIntent: PendingIntent?
    ) = NotificationCompat.Builder(this, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_baseline_notifications_24)
        .setContentTitle(title)
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)

    private fun setupExpandableNotification(notificationBuilder: NotificationCompat.Builder) {
        notificationBuilder.setStyle(
            NotificationCompat.BigTextStyle()
                .bigText(
                    """😀😁😂🤣😃😄😅😆😉😊😋😎😍😘🥰😗😙😚☺🙂🤗🤩🤔🤨😐😑😶🙄😏😣😥😮🤐😯😪😫🥱😴
                    |😌😛😜😝🤤😒😓😔😕🙃🤑😲☹🙁😖😞😟😤😀😁😂🤣😃😄😅😆😉😊😋😎😍😘🥰😗😙😚☺🙂
                    |🤗🤩🤔🤨😐😑😶🙄😏😣😥😮🤐😯😪😫🥱😴😌😛😜😝🤤😒😓😔😕🙃🤑😲☹🙁😖😞😟😤😀
                    |😁😂🤣😃😄😅😆😉😊😋😎😍😘🥰😗😙😚☺🙂🤗🤩🤔🤨😐😑😶🙄😏😣😥😮🤐😯😪😫🥱
                    |😴😌😛😜😝🤤😒😓😔😕🙃🤑😲☹🙁😖😞😟😤""".trimMargin()
                )
        )
    }

    private fun setupCustomNotification(
        notificationBuilder: NotificationCompat.Builder,
        title: String?,
        message: String?
    ) {
        notificationBuilder
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(
                RemoteViews(packageName, R.layout.view_custom_notification).apply {
                    setTextViewText(R.id.tv_title, title)
                    setTextViewText(R.id.tv_message, message)
                }
            )
    }

    companion object {

        private const val CHANNEL_NAME = "Emoji Party"
        private const val CHANNEL_DESCRIPTION = "Emoji Party를 위한 채널"
        private const val CHANNEL_ID = "Channel ID"
    }
}