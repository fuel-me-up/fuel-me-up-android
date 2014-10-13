fuel-me-up-android
==================

This is an android app that displays car sharing vehicles of drive-now and car2go on a map and filters them by fuel level.


At these two services customers can earn bonus minutes by fueling up a car after the fuel level was below a certain threshold.
The main target of this app is to spot cars with a low fuel level so you can fuel them up and earn bonus minutes. 

__Note__: I use this app to try new stuff and learn new things. For example, I recently implemented the MVP pattern and added dependency injection with dagger to this app. This project is a kind of playground for me.

![A screenshot](./screenshot.png?raw=true)

This app uses the [fuel-me-up-server](https://github.com/mAu888/fuel-me-up-server).

# Build Instructions
Just use the gradle wrapper:

```
$ ./gradlew assembleProductionEnvironmentDebug
```
