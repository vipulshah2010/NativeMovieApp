<h1 align="center">Kotlin Multiplatform Movies</h1>

<p align="center">
  <a href="https://opensource.org/licenses/Apache-2.0"><img alt="License" src="https://img.shields.io/badge/License-Apache%202.0-blue.svg"/></a>
</p>

<p align="center">  
Movies is a simple Kotlin Multiplatform application I Presented during [Kotlin Multiplatform Development Workshop](http://2019.mobilization.pl/#talk-kotlin_native_and_multiplatform_development)<br>This project demonstrates how common logic can be consumed by Android & iOS platforms.<br>
</p>
</br>

| iOS [SwiftUI] | Android [Jetpack Compose] |
| --- | --- |
| <img src="/previews/preview_ios.gif"/> | <img src="/previews/preview_android.gif" /> |

## Library and Frameworks
- Android - [Jetpack Compose](https://developer.android.com/jetpack/compose) based,
- iOS - [SwiftUI](https://developer.apple.com/documentation/swiftui) based
- Kotlin Multiplatform
    - [multi-format reflectionless serialization](https://github.com/Kotlin/kotlinx.serialization) - generates visitor code for serializable classes
    - [kotlinx.coroutines](https://github.com/Kotlin/kotlinx.coroutines) - Library support for Kotlin coroutines with multiplatform support
    - [ktor-client-core](https://ktor.io/clients/http-client/multiplatform.html) - Multiplatform Http Client
    - [ktor-client-serialization](https://ktor.io/clients/http-client/features/json-feature.html) - Multiplatform JSON serializing and de-serializing
    - [Kingfisher](https://github.com/onevcat/Kingfisher) - Pure-Swift library for downloading and caching images from the web


## Limitations [WIP ;)]
1. Using GlobalScope in SharedCode, which ofcourse lifecycle unaware
2. Handle errors gracefully
3. Test coverage
4. Jetpack compose RatingView implementation 

## Find this repository useful? :heart:
__[follow](https://twitter.com/_vipuls)__ me on twitter! 🤩

# License
```xml
Designed and developed by 2020 vipulshah2010 (Vipul Shah)

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