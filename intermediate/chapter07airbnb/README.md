# AOP Part3 Chapter07 에어비엔비

- **Naver Map API** 사용하기
- **ViewPager2** 사용하기
- **FrameLayout** 알아보기
- **CoordinatorLayout** 사용하기
- **BottomSheetBehavior** 사용하기
- **Retrofit** 사용하기
- **Glide** 사용하기

## 에어비엔비

Naver Map API 를 이용해서 지도를 띄우고 활용할 수 있음.

Mock API 에서 예약가능 숙소 목록을 받아와서 지도에 표실할 수 있음.

BottomSheetView 를 활용해서 예약 가능 숙소 목록을 인터렉션하게 표시할 수 있음.

ViewPager2 를 활용해서 현재 보고있는 숙소를 표시할 수 있음. 숙소버튼을 눌러 현재 보고 있는 숙소를 앱 외부로 공유할 수 있음.

## Note

### side-effect

composable 함수의 스코프 밖에서 발생하는 앱의 상태 변화.

### effect

UI 를 그리지 않는 composable 함수로 composition 이 완료됐을 때 side effect 를 트리거한다.

### DisposableEffect

컴포지션이 종료되거나 파라미터로 전달된 키가 변경될 때 clean up 과정이 필요한 side effect 의 경우 DisposableEffect 를 사용한다.

DisposableEffect 는 키가 변경될 때 onDispose 를 호출하여 clean up 작업을 수행하고 effect 를 다시 실행한다.

DisposableEffect 에 사용할 수 있는 키의 예시는 다음과 같다.

- effect 가 관찰해야 하는 Observable 객체
- 값이 변경될 때 필수적으로 취소-재실행 동작을 수행해야 하는 고유한 파라미터

### Accompanist

> group of libraries that anim to supplement Jetpack Compose with features that are commonly required by developers but not yet available.

새로 추가될 Compose API 를 위한 실험실 API 라고 할 수 있다.

문서를 참조하여 Compose version 과 매치되는 Accompanist version 을 사용해야 한다. 최신 버전의 Accompanist 를 사용한다면 추이적 종속성에
의해 Compose version 또한 업그레이드된다.

- System UI Controller: 컴포즈에서 시스템 UI 바의 색상 변경을 지원하는 유틸.
- AppCompat Compose Theme Adapter: xml 에서 작성한 application theme 을 컴포즈에서 사용할 수 있도록 지원하는 어댑터.
- Pager: 컴포즈에서 ViewPager 처럼 페이지화된 레이아웃을 제공.
- Permissions: 컴포즈에서 Android runtime permission 을 사용할 수 있도록 지원.
- Placeholder: 컨텐트를 로드하는 동안 쉽게 placeholder 를 사용할 수 있도록 지원.
- Flow Layouts: similar to Flex-box layout
- Navigation-Animation: 컴포즈 내비게이션 애니메이션을 제공.
- Navigation-Material: Navigation Compose 에 modal bottom sheet 같은 Material Component 를 제공.
- Drawable Painter: 컴포즈에서 안드로이드 drawable 을 사용할 수 있도록 지원.
- Swipe to Refresh: similar to SwipeRefreshLayout
- Web: WebView wrapper.

### LaunchedEffect

Composable 내에서 코루틴을 안전하게 호출하기 위해 사용하는 effect.

LaunchedEffect 는 컴포지션에 참여할 때, 파라미터로 받은 코루틴 블럭을 실행한다. 실행된 코루틴은 LaunchedEffect 가 컴포지션 과정에 더 이상 참여하지 않을
때 취소된다. 또한 LaunchedEffect 는 파라미터로 받은 Key 가 변경되고 리컴포지션됐을 때, 기존의 실행중인 코루틴을 취소하고 새 코루틴을 다시 실행한다.

컴포저블의 수명 주기 동안 한 번만 이펙트를 트리거하려면 LaunchedEffect 의 파라미터로 true 같은 상수를 전달한다.

### snapshotFlow

snapshotFlow 를 사용하여 State<T> 객체를 cold flow 로 변환할 수 있다.

snapshotFlow 에서 읽은 객체가 변경되면 snapshotFlow 는 기존의 값과 변경된 값을 비교하여 두 값이 다른 경우 새 값을 collector 에게 emit 한다.
(Flow.distinctUntilChanged 와 유사하다)