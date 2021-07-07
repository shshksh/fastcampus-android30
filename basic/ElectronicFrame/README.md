# AOP Part2 Chapter05 전자액자

## 개요

저장소 접근 권한을 이용하여 로컬 사진을 로드할 수 있음.

추가한 사진들을 일정한 간격으로 전환하여 보여줄 수 있음.

## 학습 목표

- **Layout**을 그리는 법
  - 가로 화면으로 그리기
- **Android Permission** 사용하기
- **View Animation** 사용하기
- **Activity Lifecycle** 알아보기
- Content Provider
  - SAF (Storage ACcess Framework)

## 복기

### 자투리

```xml
app:layout_constraintDimensionRatio="H,3:1"
```

`ConstraintLayout`에서 뷰의 사이즈를 비율로 지정하는 속성

```kotlin
Intent(Intent.ACTION_GET_CONTENT)
```

SAF를 사용하여 안드로이드에 내장된 액티비티를 실행하는 Intent 옵션

### 권한 요청

[Developers-권한](https://developer.android.com/guide/topics/permissions/overview)

런타임 권한은 되도록 해당 권한이 꼭 **필요한** 경우에 요청하는 것이 좋다. 또한 사용자가 앱에 대해 권한을 부여하는 것에 거부감을 느끼지 않도록 권한을 요청하는 이유를 명확히 밝히는 것이 좋다. 사용자는 런타임 권한을 요청 받을 때 수행중인 작업을 중단하며 해당 권한을 수락할지 결정해야 한다. 권한이 필요한 이유를 납득하지 못한다면 권한 요청을 거부할 수 있다.

API 레벨 23 이상의 기기에 설치된 앱에서 제공하는 기능을 위해 필수적으로 런타임 권한을 요청해야 한다면 다음 순서에 따라 권한을 요청할 수 있다. 런타임 권한을 선언하지 않았거나 API 레벨이 22 이하라면 앱을 설치할 때 권한이 자동으로 부여되므로 다음 단계를 따르지 않아도 된다.

1. 앱 **매니페스트**에 요청할 권한 선언

   앱의 manifest 파일에서 `<uses-permission>` 태그를 사용하여 요청할 권한을 미리 선언한다.

2. 권한이 **필요한 시점**까지 요청 대기

3. 권한이 필요할 때 **이미** 사용자가 필요한 권한을 부여했는지 확인

4. 권한 부여가 필요하다면 사용자에게 해당 권한이 필요한 **이유**를 표시

![img](https://developer.android.com/images/training/permissions/workflow-runtime.svg)

**권한 부여 확인**

사용자가 이미 해당 앱에 특정 권한을 부여했는지 확인하려면 `ContextCompat.checkSelfPermission()` 메서드에 요청할 권한을 전달한다. 해당 메서드는 앱에 권한이 있는지에 따라 `PERMISSION_GRANTED` 또는 `PERMISSION_DENIED` 를 반환한다.

**권한이 필요한 이유 설명**

`ContextCompat.checkSelfPermission()` 가 `PERMISSION_DENIED` 를 반환했다면 `shouldShowRequestPermissionRationale()` 메서드를 호출한다. 해당 메서드는 API 23 이상에서 호출할 수 있으며 메서드가 `true` 를 반환하는 경우 해당 권한이 필요한 이유를 설명하는 교육용 UI를 사용자에게 표시한다.

**권한 요청**

권한 요청은 `RequestPermission Contract`를 사용하여 콜백 방식으로 수행한다. 단일 권한을 요청하려면 `RequestPermission`을, 여러 권한을 동시에 요청하려면 `RequestMultiplePermissions`를 사용할 수 있다.

다음 코드 스니펫은 권한을 요청하는 `ActivityResultLauncher`를 정의하는 방법을 보여준다.

```kotlin
// Register the permissions callback, which handles the user's response to the
// system permissions dialog. Save the return value, an instance of
// ActivityResultLauncher. You can use either a val, as shown in this snippet,
// or a lateinit var in your onAttach() or onCreate() method.
val requestPermissionLauncher =
    registerForActivityResult(RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission is granted. Continue the action or workflow in your
            // app.
        } else {
            // Explain to the user that the feature is unavailable because the
            // features requires a permission that the user has denied. At the
            // same time, respect the user's decision. Don't link to system
            // settings in an effort to convince the user to change their
            // decision.
        }
    }
```

다음 코드 스니펫은 권한을 확인하고 필요할 때 권한을 요청하는 권장 프로세스를 보여준다.

```kotlin
when {
    ContextCompat.checkSelfPermission(
            CONTEXT,
            Manifest.permission.REQUESTED_PERMISSION
            ) == PackageManager.PERMISSION_GRANTED -> {
        // You can use the API that requires the permission.
    }
    shouldShowRequestPermissionRationale(...) -> {
        // In an educational UI, explain to the user why your app requires this
        // permission for a specific feature to behave as expected. In this UI,
        // include a "cancel" or "no thanks" button that allows the user to
        // continue using your app without granting the permission.
        showInContextUI(...)
    }
    else -> {
        // You can directly ask for the permission.
        // The registered ActivityResultCallback gets the result of this request.
        requestPermissionLauncher.launch(
                Manifest.permission.REQUESTED_PERMISSION)
    }
}
```

이러한 방식은 권한을 요청할 때 Request Code를 직접 관리하지 않아도 된다는 이점이 있다.

### Result API

액티비티에서 다른 액티비티를 실행하고 결과를 받아야 할 때 기존에는 `startActivityForResult()` 메서드와 `onActivityResult()` 메서드를 사용했지만 현재는 AndroidX에 도입된 **Activity Result API** 가 권장되고 있다.

Activity Result API는 콜백의 등록과 액티비티 실행 두 단계로 구분된다.

**콜백 등록**

먼저 `registerForActivityResult()` API를 통해 결과 콜백을 등록한다. 메서드는 파라미터로 `ActivityResultContract`와 `ActivityResultCallback`을 받으며, Result API는 일반적인 경우 사용할 수 있는 다수의 기본 Contracts를 제공한다.

```kotlin
val getContent = registerForActivityResult(GetContent()) { uri: Uri? ->
    // Handle the returned Uri
}
```

API 제공하는 기본 Contract 목록은 [여기서](https://developer.android.com/reference/androidx/activity/result/contract/ActivityResultContracts) 확인할 수 있다.

`registerForActivityResult()` 메서드는 `ActivityResultLauncher` 인스턴스를 반환하는데 해당 메서드는 생명주기 상태가 **STARTED** 이전에 호출되어야 하기 때문에 액티비티 또는 프래그먼트 클래스의 멤버 변수로 지정하는 것이 좋다.

**액티비티 실행**

액티비티는 결과로 받은 `ActivityResultLauncher` 인스턴스의 `launch()` 메서드를 호출하여 실행할 수 있다. `launch()` 메서드는 Contract에서 정의한 **입력** 타입과 동일한 타입을 **파라미터**로 받으며, 사용자가 후속 액티비티 실행을 종료하고 반환할 때 `registerForActivityResult()`에서 등록한 **콜백**을 실행한다.

### SAF

API 19 에서 도입된 기능으로 사용자가 문서, 이미지 등 각종 파일을 탐색하고 여는데 통일된 UI를 제공한다. 앱 개발자는 파일을 탐색하는 UI를 직접 구성할 필요 없이 SAF를 사용하여 쉽게 파일에 접근할 수 있다.

**옵션**

- ACTION_GET_CONTENT
- ACTION_CREATE_DOCUMENT
- ACTION_OPEN_DOCUMENT
- ACTION_OPEN_DOCUMENT_TREE

## 변경 사항

- 뷰 바인딩 적용
- 액티비티 실행 및 권한 요청에 Activity Result API 적용
