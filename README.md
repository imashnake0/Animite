![brah](https://github.com/user-attachments/assets/18f1c1bf-f016-4e60-a92a-f63ca03749f7)

# Animite [![build-debug](https://github.com/imashnake0/Animite/actions/workflows/build-debug.yaml/badge.svg)](https://github.com/imashnake0/Animite/actions/workflows/build-debug.yaml)
<a href="https://discord.gg/HEB7duYdqe">
    <picture>
        <source media="(prefers-color-scheme: dark)" srcset="resources/discord-assistive-chip-dark.svg">
        <img alt="Discord chip." src="resources/discord-assistive-chip-light.svg">
    </picture>
</a>

[Animite](https://github.com/users/imashnake0/projects/1) is an unofficial Android (and potentially iOS) client for [AniList](https://anilist.co/) (and potentially [MAL](https://myanimelist.net/)).

## Stack
The app tries to make use of the latest libraries and APIs:
- **Architecture:** [MVVM](https://developer.android.com/topic/architecture).
- **Networking:** [Apollo Kotlin](https://github.com/apollographql/apollo-kotlin), [Kotlin Coroutines](https://github.com/Kotlin/kotlinx.coroutines), and [COIL](https://coil-kt.github.io/coil/).
- **UI/UX:** Made purely with [Jetpack Compose](https://developer.android.com/jetpack/compose); assets using [Figma](https://www.figma.com/).
- **Dependency Injection:** [Hilt](https://developer.android.com/training/dependency-injection/hilt-android).
- **Gradle:** [Version catalogs](https://docs.gradle.org/current/userguide/platforms.html).

### Architecture
At the moment, the app uses [Google's choice for app architecture](https://developer.android.com/topic/architecture), MVVM, since I don't know any better.

## Shoutouts
Some apps that I referred to while developing:
- [AniHyou](https://github.com/axiel7/AniHyou-android)
- [Katana](https://github.com/alvr/katana)
- [AniTrend](https://github.com/AniTrend/anitrend-v2)

Libraries:
- [Compose Markdown](https://github.com/boswelja/compose-markdown)
- [MaterialKolor](https://github.com/jordond/MaterialKolor)

## [License](https://github.com/imashnake0/Animite/blob/15eaac4a80c1e6eef3a4d6c861cab05670fb174b/LICENSE.txt)
```
Copyright 2022 imashnake0

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
