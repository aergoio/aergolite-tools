# Android Studio sample project

This folder contains a sample project for an application that uses AergoLite and runs
on Android.


## Compiling

First you need to generate the aar file using the
[SQLite Android Bindings](../../wrappers/SQLite_Android_Bindings).

Then copy the generated file to the `aergolite-release` folder:

```
cp ../../wrappers/SQLite_Android_Bindings/build/outputs/aar/sqlite3-release.aar aergolite-release/aergolite-release.aar
```

Then you can open the project on Android Studio and generate the apk.
