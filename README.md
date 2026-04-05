<h1 align="center">Kotlin Multiplatform Movies</h1>

<p align="center">
  <a href="https://opensource.org/licenses/Apache-2.0"><img alt="License" src="https://img.shields.io/badge/License-Apache%202.0-blue.svg"/></a>
</p>

<p align="center">
A Kotlin Multiplatform movie listing app showing <em>Now Playing</em> films via the TMDB API.<br>
Shared network/domain logic consumed by Android (Jetpack Compose) and iOS (SwiftUI).
</p>

---

| iOS [SwiftUI]                           | Android [Jetpack Compose] |
|-----------------------------------------| --- |
| <img src="/previews/preview_ios.png" width="250"/> | <img src="/previews/preview_android.png" width="250"/> |

---

## Tech Stack

### Shared (`commonMain`)
| Library | Version | Purpose |
|---------|---------|---------|
| Kotlin Multiplatform | 2.1.21 | Shared code targeting Android + iOS |
| Ktor 3 (CIO / Darwin) | 3.1.3 | HTTP client (CIO on Android, Darwin on iOS) |
| kotlinx.serialization | 1.7.3 | JSON deserialization |
| kotlinx.coroutines | 1.9.0 | Async via `suspend` / `Flow` |

### Android
| Library | Version | Purpose |
|---------|---------|---------|
| Jetpack Compose BOM | 2024.12.01 | UI toolkit |
| Material 3 | (BOM) | Theming & components |
| Coil 3 | 3.0.4 | Image loading (`AsyncImage`) |
| Lifecycle / ViewModel | 2.8.7 | State management via `StateFlow` |

### iOS
| Technology | Purpose |
|------------|---------|
| SwiftUI | UI framework |
| `AsyncImage` | Image loading (no third-party library) |
| Swift `async/await` via `Task {}` | Consuming Kotlin `suspend` functions |

---

## Project Structure

```
.
├── SharedCode/                 # KMP shared module
│   └── src/
│       ├── commonMain/kotlin/
│       │   ├── model/          # Movie, Movies data classes
│       │   ├── MoviesRepository.kt
│       │   └── HttpClientFactory.kt  (expect)
│       ├── androidMain/kotlin/
│       │   └── HttpClientFactory.kt  (actual — CIO engine)
│       ├── iosMain/kotlin/
│       │   └── HttpClientFactory.kt  (actual — Darwin engine)
│       └── commonTest/kotlin/
│           └── MoviesDeserializationTest.kt
├── app/                        # Android application module
│   └── src/main/java/com/vipul/movieapp/
│       ├── MainActivity.kt     # ComponentActivity + Compose UI
│       ├── MovieViewModel.kt   # StateFlow-based ViewModel
│       ├── MovieResult.kt      # Sealed class for UI state
│       └── ui/theme/
│           └── MovieAppTheme.kt
└── iOSMovie/                   # Xcode project (SwiftUI)
    └── iOSMovie/
        ├── ContentView.swift
        ├── MovieService.swift  # @MainActor ObservableObject
        └── ProgressBar.swift
```

---

## Setup

### Prerequisites
- Android Studio Meerkat or newer
- Xcode 15+ (for iOS)
- JDK 17
- Gradle 8.13 (wrapper included)

### Android

1. Open the repo root in Android Studio.
2. Let Gradle sync complete.
3. Run the `:app` configuration on an emulator or device (API 26+, API 35 recommended).

```bash
./gradlew :app:assembleDebug
```

### Shared module tests

```bash
./gradlew :SharedCode:test
```

### iOS

> **Note:** iOS requires building the `SharedCode` framework first.

1. From the repo root, run:
   ```bash
   ./gradlew :SharedCode:buildForXcode
   ```
   This produces `SharedCode/build/xcode-frameworks/SharedCode.framework`.

2. In Xcode, verify the framework search path in the `iOSMovie` target points to `../SharedCode/build/xcode-frameworks`.

3. Open `iOSMovie/iOSMovie.xcodeproj` and build the `iOSMovie` scheme.

---

## Key Architecture Decisions

- **No `GlobalScope`** — coroutines are scoped to `viewModelScope` (Android) and `Task {}` tied to SwiftUI's `.task {}` modifier (iOS).
- **`suspend fun` API** — `MoviesRepository.getMovies()` is a plain `suspend fun` annotated with `@Throws`. Kotlin 2.x KMP exports it as `async throws` in Swift.
- **Result handling** — errors are caught in the ViewModel and surfaced as `MovieResult.Error`; no silent failures.
- **Image loading** — URLs only are shared; actual loading is platform-native (Coil 3 on Android, `AsyncImage` on iOS).
- **Version catalog** — all dependencies declared in `gradle/libs.versions.toml`.

---

## License

```
Copyright 2020 Vipul Shah

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
