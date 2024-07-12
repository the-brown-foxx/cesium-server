# Cesium Server
Cesium is a time-based OTP (TOTP) server. It is built with Ktor and runs on the Java Virtual Machine. The server generates a TOTP secret for each accessor, which can be checked via the `GET /accessors` endpoint, or by using the recommended client app. The secret can be keyed into the accessor's Google Authenticator app, which will generate a TOTP in sync with the server. The TOTP can be verified using the `GET /accessors/{id}/{totp}` endpoint.

## Cesium Door
This application is a generic TOTP server that can be used for any application that might require TOTP. However, it was primarily designed for the Cesium Door, which is a TOTP door created by our class as a project for one of our college courses. The door uses the `GET /accessors/{id}/{totp}` endpoint to check for an accessor's authentication. If used with the door and the Android client app, you do not need to mess with the API yourself.

## Run the server

### Install Java
To run the server, make sure that you have Java installed on your machine.
You can get it [here](https://www.java.com/en/download/manual.jsp).
The application runs on a minimum Java version of 17.

### Download the JAR
Then, download the JAR file [here](https://github.com/the-brown-foxx/cesium-server/releases/tag/0.0.1).

### Run the JAR
Before running the JAR, set the AES key seed as the environment variable `AES_KEY_SEED`. If you are planning to use the prebuilt Android client from [Cesium Android Releases](https://github.com/the-brown-foxx/cesium-android/releases/tag/1.0), use the key `6969696969696969696`.
After that, you can run the JAR.

Here is an example using Windows Powershell.
```
$Env:AES_KEY_SEED = 6969696969696969696
java -jar cesium-server.jar
```

> The server defaults to using the password `password` until set.

## Client
We recommend using the companion Android application for managing your accessors. You can get it [here](https://github.com/the-brown-foxx/cesium-android).