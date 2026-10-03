# Freelook for OneClient

A freelook mod for [OneClient](https://polyfrost.org/projects/oneclient) (Minecraft 1.8.9, Ornithe + OneConfig).

Hold or toggle a key to swing the camera around your player in third person while your movement, aiming and the rotation sent to the server stay exactly where they were. Let go and the view snaps back, same idea as Lunar's and Badlion's freelook.

> [!CAUTION]
> If you have OneClient's **BehindYou** mod with **SnapLook** enabled, it fights Freelook for control of the camera and breaks it. Turn off BehindYou (or its SnapLook feature) while using Freelook.

## Settings (OneConfig)

- **Mode**: Hold or Toggle
- **Freelook Key**: default Left Alt
- **Perspective**: Behind or In Front
- **Sensitivity**: camera turn speed, on top of your mouse sensitivity
- **Invert Yaw** / **Invert Pitch**

## Install

1. Build the jar (see below) or grab a release.
2. Drop `freelook-x.x.x.jar` into your OneClient instance's `mods` folder.
3. Launch, then open the config from OneConfig's mod list.

## Build

OneConfig's 1.8.9-ornithe build targets **Java 25**, so you need a **JDK 25 or newer** installed (the Gradle toolchain is set to 26 in `build.gradle.kts`; change it to match your JDK if needed). Then:

```
./gradlew build
```

The mod jar lands in `build/libs/`. It is a thin add-on that relies on OneClient's bundled OneConfig and Ornithe libraries at runtime.

## How it works

Built on the Ornithe toolchain (Fabric Loom + Ploceus, feather mappings) with [OneConfig](https://github.com/Polyfrost/OneConfig) for the settings UI, following Polyfrost's PolyZoom as a reference. While active, the mouse is diverted into a separate camera yaw/pitch, and the world camera is built from those angles and restored immediately after, so the player model, aim and server rotation keep using the real rotation.
