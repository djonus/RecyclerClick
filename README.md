[![](https://jitpack.io/v/djonus/RecyclerClick.svg)](https://jitpack.io/#djonus/RecyclerClick)
# RecyclerClick

### Features
* Reactive kotlin extension to receive `RecyclerView` item clicks
* Supports nested view clicks
* Supports multiple subscriptions on same view id
* Clicks stream is automatically terminated when `RecyclerView` detaches

### Examples
#### Kotlin
```
recyclerView.clicks(R.id.my_item).subscibe {
  //TODO handle it.position and it.view
}

recyclerView.clicks(R.id.my_nested_item_view).subscibe {
  //TODO handle it.position and it.view
}
```
#### Java
```
RecyclerClickKt.clicks(recyclerView, R.id.my_item).subscibe(click -> {
  //TODO handle click.position and click.view
});

RecyclerClickKt.clicks(recyclerView, R.id.my_nested_item_view)
  .subscibe(click -> {
    //TODO handle click.position and click.view
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
  implementation 'com.github.djonus:RecyclerClick:0.1.0'
}
```
