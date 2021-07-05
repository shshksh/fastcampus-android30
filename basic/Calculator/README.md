# AOP Part2 Chapter04 계산기

## 학습 목표

- **Layout**을 그리는 법
  -  **TableLayout** 사용하기
  - **ConstraintLayout** 사용하기
  - **LayoutInflater** 사용하기
- **Thread** 사용하기
  - 새 Thread를 만들어서 사용하기
  - **runOnUiThread** 사용하기
- **Room** 사용하기



## 복기

### 안드로이드의 백그라운드 스레드

**참고 문서**

[메인 스레드와 핸들러](https://www.notion.so/shshksh/2-Handler-425fa7f9f34b4f7fb25a134376f8514e#a037e87b39144b4e9d592143330427b4)

[백그라운드 스레드](https://www.notion.so/shshksh/3-ba1713208b6f4d1fba97aff033214f61#9425d584acef4e1da3086ed546ac978c)

[루퍼, 스레드, 메시지 큐, 핸들러](https://www.notion.so/shshksh/3632f1b09c8d482e81e51671dda26844)

안드로이드에서는 작업 스레드를 따로 명시하지 않는다면 모든 컴포넌트에서의 작업은 **메인 스레드**(UI 스레드)에서 수행된다. 이는 작업에 많은 시간이 소요되는 경우 문제가 될 수 있는데 작업을 수행하는 동안 스레드를 블로킹하기 때문에 사용자의 입력에 대한 응답이 지연되며, 사용자는 작업이 수행되는 동안 앱이 중지되었다고 느끼거나 앱의 반응성이 떨어진다고 느낄 수 있다. 따라서 대량의 데이터를 사용하여 연산하는 CPU Bound 작업(정렬, 파싱, ...)이나 I/O Bound 작업(파일, 네트워킹, ...)은 **백그라운드 스레드**에서 수행되어야 하며 경우에 따라 백그라운드에서의 수행이 강제될 수 있다.

### 메인 스레드의 작업 처리

안드로이드의 메인 스레드는 일반적인 자바/코틀린 애플리케이션과는 조금 다른 방식으로 작업을 처리한다. 메인 스레드는 작업을 처리하기 위해 `Thread`, `Looper`, `Handler` 클래스를 사용하며 각각의 클래스의 역할은 다음과 같다.

**Looper**

> thread의 메시지 루프를 실행하기 위해 사용되는 클래스. 스레드는 기본적으로 메시지 루프를 가지지 않지만 스레드 내에서 Looper.prepare() 메서드와 Looper.loop() 메서드를 호출함으로 Looper를 생성할 수 있다.

```java
class LooperThread extends Thread {
    public Handler mHandler;
    public void run() {
        Looper.prepare();
        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                // process incoming messages here
            }
        };
        Looper.loop();
    }
}
```

Looper는 스레드의 TLS(Thread Local Storage)에 저장된다.

메인 스레드 또한 자신만의 Looper를 가진다. Looper.getMainLooper()를 호출하면 어디서든 메인 스레드의 Looper를 얻을 수 있다.

각각의 Looper는 MessageQueue를 가진다. 스레드에서 수행될 작업은 Message 객체로 래핑되며 작업을 수행할 스레드 내부 Looper의 MessageQueue에 들어가 순차적으로 수행된다. Looper.loop() 메서드 내에서는 무한 루프가 있으며 루프 내에서 메시지 큐로부터 다음 메시지를 얻고 디스패치하는 작업이 반복된다.

**Handler**

> Handler 클래스는 스레드의 메시지 큐와 연결된 Runnable 인스턴스 또는 Message 인스턴스를 보내거나 처리하는데 사용된다. 각각의 Handler 인스턴스는 단일 스레드 내에서 메시지 큐와 연결된다. Handler를 사용하는 두 가지 주요 목적이 있는데 하나는 메시지나 runnable 인스턴스를 특정 시점에 실행하도록 스케줄링 하기 위해서이며, 다른 하나는 다른 스레드에서 수행될 작업을 메시지 큐에 넣기 위해서 사용된다.

Handler는 기본적으로 Looper에 바인딩된다. 스레드에서 Handler의 기본 생성자를 호출하는 것은 해당 스레드의 Looper와 바인딩된다는 것을 의미하며 따라서 Looper가 준비되지 않은 상태(prepare() 메서드를 호출하기 이전)에서 Handler의 기본 생성자를 사용하여 인스턴스를 생성하면 RuntimeException이 발생한다.

특정 Looper 인스턴스를 사용하여 Handler 인스턴스를 생성할 수도 있다. 이는 특정 작업을 메인 스레드에서 수행해야 할 때 유용하게 사용된다. Looper.getMainLooper() 메서드를 사용하여 메인 루퍼의 참조를 얻고 해당 루퍼로 핸들러를 생성하면 생성된 핸들러를 사용하여 메인 스레드에서 수행될 작업을 보낼 수 있다.

**HandlerThread**

앞에서 설명했듯 스레드는 기본적으로 Looper를 가지지 않는다는 문제가 있다. 따라서 스레드에 대한 관리가 필요할 때 마다 Thread 클래스를 상속하는 클래스를 정의해야 하지만 이 과정을 줄이기 위해 HandlerThread 클래스를 사용할 수 있다.

HandlerThread는 Thread를 상속하는 클래스로 내부적으로 Looper를 사용하도록 동작한다. 따라서 HanlerThread를 사용하면 Looper에 대한 작업을 처리할 필요가 없다.

**목적**

안드로이드의 메인 스레드는는 왜 이렇게 복잡한 방식으로 작업으로 작업을 처리하는 것일까? 앞에서 소개한 클래스들을 사용하는 목적 중 하나로 UI의 동기화 문제가 있다. 백그라운드 스레드에서 UI 요소에 자유롭게 접근하여 상태를 변경할 수 있다면 UI 상태 변경 순서가 보장되지 않는다. 하지만 메인 스레드에서 루퍼를 사용하여 각각의 메시지를 순차적으로 처리하기 때문에 다른 스레드에서 여러 메시지를 동시에 보내더라도 순차적으로 처리할 수 있다.

이처럼 Looper를 사용하면 작업의 순서를 보장할 수 있다. 예를 들어 좋아요 버튼의 터치 마다 변경된 결과를 DB에 반영해야 하는 상황을 가정해보자. 각각의 이벤트마다 새로운 스레드를 생성하여 작업을 처리한다면 스레드의 동작 순서가 보장되지 않기 때문에 UI 상태와 실제 데이터가 불일치하는 상황이 발생할 수 있다. 이때 HandlerThread 등을 사용하여 루퍼를 통해 메시지를 전달한다면 작업을 순차적으로 처리할 수 있기 때문에 앞의 문제를 해결할 수 있다.

### Room

안드로이드 애플리케이션이 사용하는 로컬 데이터베이스의 ORM. SQLite에 대한 추상화 계층을 제공하여 쉽게 로컬 데이터베이스를 사용할 수 있다.

**참고 문서**

[Room](https://www.notion.so/shshksh/Room-61b5c1a23f0744fa99e96c3d61d5e0c8)

