![Figma mock-up of the Home screen.](resources/Home_Pixel_5.png)

# Animite 🚧
[Animite](https://github.com/users/imashnake0/projects/1) is an unofficial client for [AniList](https://anilist.co/) (and potentially [MAL](https://myanimelist.net/)).

## Stack
The app makes use of the *latest libraries and APIs* so be prepared to see `α`s and `β`s all over the place:
- **Architecture:** [**MVVM**](https://developer.android.com/topic/architecture).
- **Networking:** [Apollo Kotlin](https://github.com/apollographql/apollo-kotlin), [Kotlin Coroutines](https://github.com/Kotlin/kotlinx.coroutines), and [COIL](https://coil-kt.github.io/coil/).
- **UI/UX:** Made purely with [Jetpack Compose](https://developer.android.com/jetpack/compose); assets using [Figma](https://www.figma.com/).
- **Dependency Injection:** [Hilt](https://developer.android.com/training/dependency-injection/hilt-android).
- **Gradle:** [Version catalogs](https://docs.gradle.org/current/userguide/platforms.html) and [refreshVersions](https://github.com/jmfayard/refreshVersions).

### Architecture
At the moment, the app uses Google's choice for app architecture, MVVM, since I don't know any better. The app has the following structure:
<details>
    <summary> 
        <b> <code> tree </code> </b> 
    </summary>
    <p>
        <pre>
            <code>
                animite
                ├── AnimiteApplication.kt
                ├── data
                │   ├── repos
                │   │   ├── MediaListRepository.kt
                │   │   └── MediaRepository.kt
                │   └── sauce
                │       ├── apis
                │       │   ├── apollo
                │       │   │   ├── Apollo.kt
                │       │   │   ├── ApolloMediaApi.kt
                │       │   │   └── ApolloMediaListApi.kt
                │       │   ├── MediaApi.kt
                │       │   └── MediaListApi.kt
                │       ├── MediaListNetworkSource.kt
                │       └── MediaNetworkSource.kt
                ├── dev
                │   ├── extensions
                │   └── internal
                │       ├── Constants.kt
                │       └── Path.kt
                ├── di
                │   └── NetworkModule.kt
                └── ui
                    ├── MainActivity.kt
                    ├── elements
                    │   ├── home
                    │   │   ├── Home.kt
                    │   │   ├── MediaSmall.kt
                    │   │   └── MediaSmallRow.kt
                    │   ├── profile
                    │   │   └── Profile.kt
                    │   └── rslash
                    │       └── RSlash.kt
                    ├── state
                    │   ├── HomeUiState.kt
                    │   └── HomeViewModel.kt
                    └── theme
                        ├── Color.kt
                        ├── Shape.kt
                        ├── Theme.kt
                        └── Type.kt
            </code>
        </pre>
    </p>
</details>

#### Data Layer
[comment]: # (`tree -I '*.md'`)
```
data
├── repos
│   ├── MediaListRepository.kt
│   └── MediaRepository.kt
└── sauce
    ├── apis
    │   ├── apollo
    │   │   ├── Apollo.kt
    │   │   ├── ApolloMediaApi.kt
    │   │   └── ApolloMediaListApi.kt
    │   ├── MediaApi.kt
    │   └── MediaListApi.kt
    ├── MediaListNetworkSource.kt
    └── MediaNetworkSource.kt
```

#### UI Layer
[comment]: # (`tree -I '*.md'`)
```
ui
├── MainActivity.kt
├── elements
│   ├── home
│   │   ├── Home.kt
│   │   ├── MediaSmall.kt
│   │   └── MediaSmallRow.kt
│   ├── profile
│   │   └── Profile.kt
│   └── rslash
│       └── RSlash.kt
├── state
│   ├── HomeUiState.kt
│   └── HomeViewModel.kt
└── theme
    ├── Color.kt
    ├── Shape.kt
    ├── Theme.kt
    └── Type.kt
```

## [License](https://github.com/imashnake0/Animite/blob/15eaac4a80c1e6eef3a4d6c861cab05670fb174b/LICENSE.txt)
```
Copyright 2022 Kamalesh Reddy Paluru

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
