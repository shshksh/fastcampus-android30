# AOP Part2 08 심플 웹 브라우저

- 웹 사이트를 불러올 수 있다.
- 뒤로 가기, 앞으로 가기 기능이 있다.
- 홈 버튼을 눌러 처음으로 돌아갈 수 있다.
- 웹 사이트의 로딩 정도를 확인할 수 있다.

## 활용 기술

- ConstraintLayout
- EditText
- WebView

## 웹 뷰

안드로이드에서 웹 뷰를 사용할 때는 기본 동작으로 링크를 처리할 수 있는 앱을 실행한다. 따라서 아무런 옵션 없이 URL을 로드하면 기본 브라우저인 크롬 앱을 사용하여 웹 사이트가 열린다. 이를 웹 뷰에서 열기 위해선 webViewClient를 추가해야 한다.

내부 구현을 보면 안드로이드의 다른 뷰를 봤을 때와 달리 클래스의 사이즈가 상당히 작다. 다른 기능은 어짜피 웹 뷰에서 띄울 페이지와 자바 스크립트로 만들 수 있으니 이정도로도 충분하단 걸까? 메서드에 대한 문서도 없고(필요 없을 만큼 직관적이긴 하다) 실제 웹 앱을 돌리기 충분한지는 모르겠지만 큰 관심은 가지 않는다. 네이티브 앱과 같이 쓰려면 간단한 공지 사항같은 페이지를 빨리 보여줘야 할때나 쓸만하지 싶다.

### 예시

```kotlin
webView.apply {
    webViewClient = WebViewClient()
    webChromeClient = WebChromeClient()
    settings.javaScriptEnabled = true
    loadUrl(DEFAULT_URL)
}
```

### WebChromeClient

클래스 내부를 보면 onProgressChanged, onReceivedTitle, onShowCustomView, onCreateWindow, onJsAlert, ... 등의 메서드가 정의되어 있는것을 볼 수 있다. 간단한 웹 페이지라면 WebViewClient 로도 충분하지만 추가적인 기능이 필요할 때 웹 뷰에 WebChromeClient를 확장하고 메서드를 오버라이드한 클래스를 추가할 수 있다. 일종의 확장 기능을 위한 플러그인 클래스라고 볼 수 있지 않을까?

## EditText

### imeOptions

EditText를 포커스를 받을 때 나타나는 가상 키보드의 액션 버튼(Enter) 동작을 명시해주는 속성

### selectAllOnFocus

EditText가 포커스를 받을 때 텍스트를 모두 선택하도록 설정하는 속성