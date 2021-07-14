# AOP Part3 Chapter03 알람앱

- **AlarmManager** 사용하기
- **Notification** 사용하기
- **Broadcast receiver** 사용하기

## 알람 앱

지정된 시간에 알람이 울리게 할 수 있음.

지정된 시간 이후에는 매일 같은 시간에 반복되기 알람이 울리게 할 수 있음.

## Background 작업의 분류

[백그라운드 처리 가이드](https://developer.android.com/guide/background?hl=ko)

- Immediate tasks (즉시 실행해야 하는 작업)

  - Thread
  - Handler
  - Kotlin coroutines

- Deffered tasks (지연된 작업)

  - WorkManager

- Exact tasks (정시에 실행해야 하는 작업)

  - AlarmManager

![백그라운드 작업에 가장 적합한 카테고리를 결정하는 데 도움이 되는 결정 트리](https://developer.android.com/images/guide/background/task-category-tree.png?hl=ko)

## AlarmManager

알람을 사용하면 애플리케이션이 사용되지 않을 때 시간 기반 작업을 실행할 수 있다.

알람매니저는 두 가지 유형의 시간을 사용한다.

- Read Time (실제 시간) 으로 실행시키는 방법
- Elapsed Time (기기가 부팅된지로부터 얼마나 지났는지) 으로 실행시키는 방법

### 잠자기 모드

[잠자기 모드 이해](https://developer.android.com/training/monitoring-device-state/doze-standby?hl=ko#understand_doze)

API 23 부터 안드로이드에 잠자기 모드와 앱 대기 모드가 도입되었다. 잠자기 모드는 기기를 오랫동안 사용하지 않는 경우 앱의 백그라운드 CPU 및 네트워크 활동을 지연시켜 배터리 소모를 줄인다.

기기가 잠자기 모드에 진입하면 몇 가지 제한사항이 발생한다.

- 네트워크 액세스의 정지
- wake lock 무시
- **표준 AlarmManager 의 알람이 잠자기 모드가 해제될 때 까지 지연**
  - 잠자기 모드에서 실행되는 알람을 설정하려면 `setAndAllowWhileIdle()` 또는 `setExactAndAllowWhileIdle()`을 사용해야 한다. 하지만 두 메서드를 사용하더라도 특정 앱에서 9분 내에 2회 이상 알람을 호출할 수는 없다. 
  - `setAlarmClock()` 으로 설정된 알람은 정상적으로 실행된다. 시스템은 이 알람이 실행되기 직전 잠자기 모드를 종료한다.
- Wi-Fi 검색을 중단한다.
- 동기화 어댑터 실행을 허용하지 않는다.
- JobScheduler 실행을 허용하지 않는다.

### 알람의 정확성

대부분의 경우 setInexactRepeating() 메서드를 사용하는 것이 좋다. 해당 메서드를 사용하면 안드로이드가 여러 개의 InexactRepeatingAlarm 을 동기화하고 동시에 실행하여 배터리 소모를 줄일 수 있다.

정확한 시간이 필수적이라면 setRepeating() 메서드를 사용한다. 하지만 이는 권장되지 않는다.
