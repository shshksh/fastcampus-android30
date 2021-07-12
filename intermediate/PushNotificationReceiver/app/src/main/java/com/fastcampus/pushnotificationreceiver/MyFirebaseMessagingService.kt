package com.fastcampus.pushnotificationreceiver

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
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

    private fun createNotification(
        type: NotificationType,
        title: String?,
        message: String?
    ): Notification {
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        when (type) {
            NotificationType.NORMAL -> Unit
            NotificationType.EXPANDABLE -> {
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
            NotificationType.CUSTOM -> {
                TODO()
            }
        }
        return notificationBuilder.build()
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

    companion object {

        private const val CHANNEL_NAME = "Emoji Party"
        private const val CHANNEL_DESCRIPTION = "Emoji Party를 위한 채널"
        private const val CHANNEL_ID = "Channel ID"
    }
}