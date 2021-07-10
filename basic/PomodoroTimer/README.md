# AOP Part2 Chapter06 뽀모도로 타이머

## 개요

- 1 ~ 60분 까지 타이머를 설정할 수 있다.
- 1초마다 화면을 갱신한다.
- 타이머 효과음을 들을 수 있다.

## 활용 기술

- ConstraintLayout
- CountDownTimer
- SoundPoll

## ConstraintLayout

### 상대적 위치 결정

뷰의 위치를 다른 뷰에 대해 상대적으로 결정할 수 있다. 대상이 되는 기준으로는 뷰의 상단(top), 하단(bottom), 좌측(left or start), 우측(right or end) 및 텍스트의 시작 높이(baseline)가 있다.

```xml
app:layout_constraintBaseline_toBaselineOf
<!--두 뷰의 텍스트 시작 높이가 같도록 위치를 지정하는 속성-->
```

### 바이어스(bias)

뷰의 양쪽에 제약을 설정하는 경우 기본적으로 뷰의 위치는 두 제약 사이의 가운데 위치하지만, bias를 설정하여 제약의 강도를 조정할 수 있다.

```xml
layout_constraintHorizontal_bias
layout_constraintVertical_bias
```

위의 속성에는 0부터 1.0의 값을 지정할 수 있으며 기본적으로 0.5의 값을 가진다.

### 뷰의 크기 지정

layout_width 또는 layout_height 속성을 0dp로 지정하는 경우 뷰의 너비 또는 높이는 해당 제약 조건에 속하는 크기만큼 증가한다. 이때 하나 이상의 속성이 0dp로 지정되었다면 다른 속성의 크기를 비율을 사용하여 상대적으로 지정할 수 있다.

```xml
<Button android:layout_width="wrap_content"
    android:layout_height="0dp"
    app:layout_constraintDimensionRatio="1:1" />
```

적용될 수 있는 값으로는 너비대 높이의 비율을 나타내는 float 값을 지정하거나, width:height 형식의 값을 지정할 수 있다.

너비와 높이의 값이 모두 0dp로 지정됐을 때 또한 비율을 적용할 수 있는데, 이때는 해당 제약 조건과 명시한 비율을 만족하는 가장 큰 사이즈의 크기를 가진다. 특정 위치에 대한 비율을 명시하기 위해 설정할 값의 접두사에 "W" 또는 "H"를 붙인 후 그 뒤에 비율을 명시한다. 뷰의 비율은 설정한 비율과 일치하며 접두사로 지정한 속성의 크기가 변경된다.

### 체인

두 개 이상의 뷰가 서로에게 양방향 제약이 설정되면 chain이 설정된다. chain에서 첫 번째 뷰가 해당 chain의 head가 되며 head 뷰의 chain 속성을 변경하여 chain을 어떻게 적용할지 변경할 수 있다.

```
layout_constraintHorizontal_chainStyle 
layout_constraintVertical_chainStyle 
```

**체인 속성**

- spread: 기본 속성. 체인에 속하는 뷰는 서로 같은 간격을 가진다.
- spread_inside: spread 속성과 비슷하지만 양 끝단에 있는 뷰는 끝단에 대한 간격을 가지지 않는다.
- packed: 체인에 속한 뷰 사이 간격을 없에고 함께 모은다.

![image](https://developer.android.com/reference/androidx/constraintlayout/widget/resources/images/chains-styles.png)

### Barrier

배리어는 다수의 뷰를 참조하는 가상의 뷰로 배리어에 속한 뷰 중 특정 방향(start, end, top, bottom, left, right)으로 가장 튀어나온 위치에 대한 기준을 잡아준다.

**예시**
![img](https://developer.android.com/reference/androidx/constraintlayout/widget/resources/images/barrier-end.png)![img](https://developer.android.com/reference/androidx/constraintlayout/widget/resources/images/barrier-adapt.png)

```xml
<androidx.constraintlayout.widget.Barrier
    android:id="@+id/barrier"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:barrierDirection="start"
    app:constraint_referenced_ids="button1,button2" />
```

### Group

그룹은 다수의 뷰를 참조하여 속하는 전체 뷰에 대해 가시성(visibility)을 한번에 제어할 수 있다.

```xml
<androidx.constraintlayout.widget.Group
    android:id="@+id/group"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:visibility="visible"
    app:constraint_referenced_ids="button4,button9" />
```

