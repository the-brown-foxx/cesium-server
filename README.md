# Cesium Server
Cesium is a time-based OTP (TOTP) server. It is built with Ktor and runs on the Java Virtual Machine.

## Run the server

### Install Java
To run the server, make sure that you have Java installed on your machine.
You can get it here: https://www.java.com/en/download/manual.jsp
The application runs on a minimum Java version of 17.

### Download the JAR
Then, download the JAR file here:
https://github.com/the-brown-foxx/cesium-server/releases/tag/0.0.1

### Run the JAR
Before running the JAR, set the AES key seed as the environment variable `AES_KEY_SEED`. If you are planning to use the prebuilt Android client from https://github.com/the-brown-foxx/cesium-android/releases/tag/1.0, use the key `6969696969696969696`.
After that, you can run the JAR.

Here is an example using Windows Powershell.
```
$Env:AES_KEY_SEED = 6969696969696969696
java -jar cesium-server.jar
```

## Client
We recommend using the companion Android application for managing your users. You can get it here: https://github.com/the-brown-foxx/cesium-android