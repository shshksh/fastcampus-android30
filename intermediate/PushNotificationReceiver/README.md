# AOP Part3 Chapter01 푸시 알람 수신기

- Firebase 토큰을 확인할 수 있다.
- 일반, 확장형, 커스텀 알림을 볼 수 있다.

## 활용 기술

- Firebase Cloud Messaging
- Notification

## Firebase Cloud Messaging

[Firebase Cloude Messaging - Android](https://firebase.google.com/docs/cloud-messaging/android/client)

### 메시지

메시지는 **알림 메시지**와 **데이터 메시지** 두 가지 종류가 있으며 모든 메시지는 수신된지 20초 이내 처리되어야 한다. 단순히 푸시 메시지를 보내려면 알림 메시지를 사용하고 데이터를 전송하여 클라이언트 앱에서 처리하려면 데이터 메시지를 사용하는 것이 좋다.

알림 메시지(Notification Message)의 경우 간단하게 보낼 수 있지만 복잡한 작업이 불가능하다. FCM SDK에 의해 처리되기 때문에 앱이 포그라운드에 있는 경우에는 푸시 알림이 표시되지 않고 onMessageReceived만 호출된다. 파이어베이스 콘솔에서 Notification Composer를 사용하거나 서버에서 FCM Server Protocols를 사용하여 푸시 메시지를 보낼 수 있다. "notification" 키를 사용한다.

데이터 메시지(Data Message)는 클라이언트 앱에서 메시지를 직접 처리하며 앱 런처 액티비티의 인텐트로 데이터가 전달된다. 서버에서 FCM Server Protocols를 사용한 전송만 가능하다. "data" 키를 사용한다.

### 토큰

FCM SDK는 앱을 시작할 때 각각의 클라이언트 앱에 대해 토큰을 발급한다. 하지만 발행된 토큰은 여러 가지 이유로 변경될 수 있다.

- 새 기기에서 앱 복원
- 사용자의 앱 재설치
- 사용자의 앱 데이터 삭제

따라서 실제 서비스 환경이라면 `onNewToken` 함수를 재정의하여 서버로 전송하는 등 변경되는 토큰에 대한 처리가 필요하다. 새 토큰이 생성될 때 마다 `onNewToken` 함수가 콜백된다.

## 알림

[알림 개요](https://developer.android.com/guide/topics/ui/notifiers/notifications?hl=ko)
[알림 메시지와 알람](https://www.notion.so/shshksh/485c94c6ab464adda390281d5686b2cd)

알림은 OS의 버전에 따라 많은 변화가 발생했다. 따라서 지원하는 버전에 맞게 필요한 동작을 수행할 수 있어야 하며 하위호환성을 유지해야 한다.

`NotificationCompat`: 알림을 어떻게 **구성** 하는지에 초점을 맞춘 클래스. 알림 타입에 맞는 스타일을 가지며 `NotificationCompat.Builder` 클래스를 사용하여 알림을 구성할 수 있다.

`NotificationManager<Compat>`: 시스템 서비스로, 실제 시스템과 연동하여 알림 채널을 생성하거나 구성된 알림을 앱에 전달할 수 있다.

## RemoteViews

[RemoteViews document](https://developer.android.com/reference/android/widget/RemoteViews?hl=ko)

다른 프로세스에서 표시되는 뷰 계층 구조를 나타내는 클래스. 알림이나 위젯에서 사용되며 적용 가능한 레이아웃이나 위젯의 종류가 제한된다.

## PendingIntent

[PendingIntent document](https://developer.android.com/reference/android/app/PendingIntent?hl=ko)

 `PendingIntent` 클래스는 **애플리케이션을 대신해** 시스템 또는 다른 애플리케이션에서 **나중에** 실행될 수 있는 인텐트를 생성하는 메커니즘을 제공한다.

펜딩 인텐트는 향후 발생할 이벤트(외부에서 알림의 터치 등)에 대한 응답으로 실행될 수 있는 인텐트를 패키징할 때 흔히 사용된다.

`PendingIntent` 클래스는 펜딩 인텐트를 구성하는 static 메서드를 제공한다. 이 메서드들은 액티비티를 시작시키거나 백그라운드/포그라운드 서비스를 시작시키며, 암시적/명시적 인텐트를 브로드캐스트하는 데 사용된다.

펜딩 인텐트는 애플리케이션의 범위 밖에서 실행되므로 펜딩 인텐트가 실행될 시점의 사용자 컨텍스트를 고려하는 것이 중요하다. 즉 액티비티를 시작시키는 펜딩 인텐트는 알림을 터치하는 것과 같은 사용자 액션에 직접 응답할 때만 사용되야 한다.