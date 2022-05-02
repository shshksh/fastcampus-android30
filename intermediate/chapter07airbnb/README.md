# AOP Part3 Chapter07 에어비엔비

- **Naver Map API** 사용하기
- **ViewPager2** 사용하기
- **FrameLayout** 알아보기
- **CoordinatorLayout** 사용하기
- **BottomSheetBehavior** 사용하기
- **Retrofit** 사용하기
- **Glide** 사용하기

## 에어비엔비

Naver Map API 를 이용해서 지도를 띄우고 활용할 수 있음. Mock API 에서 예약가능 숙소 목록을 받아와서 지도에 표실할 수 있음. BottomSheetView 를
활용해서 예약 가능 숙소 목록을 인터렉션하게 표시할 수 있음. ViewPager2 를 활용해서 현재 보고있는 숙소를 표시할 수 있음. 숙소버튼을 눌러 현재 보고 있는 숙소를 앱
외부로 공유할 수 있음.

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

## Accompanist-Pager
