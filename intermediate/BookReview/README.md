# AOP Part3 Chapter04 도서 리뷰 앱

인터파크 Open API 를 통해 베스트셀러 정보를 가져와서 화면에 그릴 수 있다.

인터파크 Open API 를 통해 검색어에 해당하는 책 목록을 가져와서 화면에 그릴 수 있다.

Local DB 를 이용하여 검색 기록을 저장하고 삭제할 수 있다.

Local DB 를 이용하여 개인 리뷰를 저장할 수 있다.

## 활용 기술

- **RecyclerView**
- **View Binding**
- **Retrofit**
- **Glide**
- **Android Room**
- Open API

## ListAdapter

RecyclerView.Adapter 를 확장하는 클래스로 리사이클러 뷰에서 사용할 리스트를 내장하고 있으며 백그라운드에서 리스트의 변경 사항을 계산한다. 리스트 어댑터는 두 가지 제네릭 파라미터를 가지는데 첫 번째 파라미터는 내부 리스트에 저장할 아이템의 타입을 받고, 두 번째 파라미터로는 어댑터에서 사용하는 뷰 홀더 클래스를 받는다. 리스트 어댑터의 submitList 메서드를 통해 어댑터에서 사용할 리스트를 전달할 수 있다.

클래스 내에는 submitList 외에도 getItem, getItemCount, getCurrentList, onCurrentListChanged 등의 메서드가 있는데 이 중 onCurrentListChanged 를 제외한 메서드는 private 필드인 mDiffer: AsyncLsitDiffer 에 동작을 위임한다. 리스트 변경에 대한 콜백을 추가할 필요가 있다면 onCurrentListChanged 메서드를 오버라이드 한다. 해당 메서드는 mDiffer 인스턴스를 생성할 때 전달되는 콜백 리스너에서 호출된다.

### AsyncListDiffer

백그라운드에서 DiffUtil 을 사용하여 submit 된 리스트의 변경 계산을 지원하는 클래스. 직접적으로 사용되기 보다는 ListAdapter 에 래핑되어 사용된다. 리스트 변경 계산 결과는 현재 리스트에 반영되기 전 ListUpdateCallback 에 디스패치된다.

생성자 파라미터로 AdapterListUpdateCallback 인스턴스를 받는데 submitList 의 호출로 리스트 아이템의 변경이 발생하면 AdapterListUpdateCallback 인스턴스의 onInserted, onRemoved, onMoved, onChanged 메서드를 호출하며, 해당 메서드 내에서 어댑터에게 변경 사항을 전달한다.

## DiffUtil.ItemCallback

ListAdpater와 AsyncListDiffer 클래스에서 리스트의 아이템이 동일한지 판별하는 메서드가 담긴 추상 클래스. 이 클래스를 확장하는 클래스는 areItemsTheSame 과 areContentsTheSame 을 오버라이드 해야 한다.

위의 두 메서드는 hash collection 에서 hashCode() 메서드와 equals() 메서드와 비슷한 역할을 수행한다. areItemsTheSame 메서드는 두 아이템의 id 값 등을 사용하여 동일한 아이템인지 확인하는 코드가 들어가며 areContentsTheSame 메서드는 areItemsTheSame 메서드가 true 를 반환했을 때만 호출되며 UI 에 따라 동작을 변경할 수 있도록 아이템의 동등성을 확인하는 코드가 들어간다.

## Room Migration

[Room 데이터베이스 이전](https://developer.android.com/training/data-storage/room/migrating-db-versions?hl=ko)

Room 에서 테이블 구조나 컬럼 수, 이름 등에 변경이 있을 경우 데이터베이스의 버전을 올리고 기존의 사용자들이 안전하게 변경 사항을 적용할 수 있도록 Migration 이 필요하다. Room 은 Migration 클래스를 통해 버전의 변경을 지원한다.

```kotlin
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE `Fruit` (`id` INTEGER, `name` TEXT, " +
                "PRIMARY KEY(`id`))")
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE Book ADD COLUMN pub_year INTEGER")
    }
}

Room.databaseBuilder(applicationContext, MyDb::class.java, "database-name")
        .addMigrations(MIGRATION_1_2, MIGRATION_2_3).build()
```
