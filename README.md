[![](https://jitpack.io/v/djonus/RecyclerClick.svg)](https://jitpack.io/#djonus/RecyclerClick)
# RecyclerClick

This is built as a basic library example.

### Features
* Kotlin extension to receive `RecyclerView` item clicks
* Supports nested view clicks
* Provides view position in recycler view adapter

### Examples
#### Kotlin
```
recyclerView.clicks(R.id.my_item) {
  //TODO handle it.position and it.view
}

recyclerView.clicks(R.id.my_nested_item_view) {
  //TODO handle it.position and it.view
}
```
#### Java
```
RecyclerClickKt.clicks(recyclerView, R.id.my_item, click -> {
  //TODO handle click.position and click.view
  return Unit.INSTANCE;
});

RecyclerClickKt.clicks(recyclerView, R.id.my_nested_item_view, click -> {
  //TODO handle click.position and click.view
  return Unit.INSTANCE;
});
```

### Gradle dependency
```
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}

dependencies {
  implementation 'com.github.djonus:RecyclerClick:1.0.2'
}
```
