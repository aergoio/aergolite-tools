# SQLite Android Bindings

This folder has a slightly modified version of the android.database.sqlite namespace.

More info [here](https://sqlite.org/android/doc/trunk/www/)

## Compiling

To generate the .so libraries and the aar file:

```
export ANDROID_HOME=~/Android/Sdk/
cd sqlite3
../gradlew assembleRelease
```

The generated aar file will be present at `sqlite3/build/outputs/aar/`
