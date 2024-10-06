# Ludi
[![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0) [![Build Status](https://img.shields.io/github/actions/workflow/status/mr3y-the-programmer/Ludi/build.yml?branch=main&label=Desktop%2FAndroid%20Build&logo=Android&logoColor=black)](https://github.com/mr3y-the-programmer/Ludi/actions/workflows/build.yml) <a target="_blank" href="https://androidweekly.net/issues/issue-600"><img src="https://androidweekly.net/issues/issue-600/badge"></a>
![mar32](https://github.com/mr3y-the-programmer/Ludi/assets/26522145/93ae21de-7bb1-4851-9263-c8a9ca0ad801)

Ludi is a Kotlin multiplatform app(Android + Desktop) For browsing & discovering new games, Checking daily updated price discounts & giveaways on games, and RSS gaming news feed reader from your favorite gaming news websites, All in one single app. 

It is a playground for demonstrating the use of modern tech stack(KMP, compose multiplatform..etc) to develop high-quality apps.

## Screenshots
### Android
<p align="center">
  <img src="https://github.com/mr3y-the-programmer/Ludi/assets/26522145/3e7f2382-212e-4e4b-a11d-f1a775b009e9" width="33%" />
  <img src="https://github.com/mr3y-the-programmer/Ludi/assets/26522145/fc15500c-4033-4f12-a809-7e5f7cf936c2" width="33%" />
  <img src="https://github.com/mr3y-the-programmer/Ludi/assets/26522145/b5646433-b4b8-4b43-a042-dcd0ca6493ea" width="33%" />
</p>
<p align="center">
  <img src="https://github.com/mr3y-the-programmer/Ludi/assets/26522145/e062becf-64d6-42a1-b10f-b092087c72fe" width="33%" />
  <img src="https://github.com/mr3y-the-programmer/Ludi/assets/26522145/968e9e20-828c-4d8e-9c66-340bdcdbd677" width="33%" />
</p>

### Desktop 
<p float="left">
  <img src="https://github.com/mr3y-the-programmer/Ludi/assets/26522145/59163d5d-b8a1-49e7-8a06-fc8598e72618" width="33%" />
  <img src="https://github.com/mr3y-the-programmer/Ludi/assets/26522145/acf823ef-acc0-4b81-b9a5-237721efb8b0" width="33%" />
  <img src="https://github.com/mr3y-the-programmer/Ludi/assets/26522145/b403060a-3b1c-4219-aca7-ef1380cb1e47" width="33%" />
</p>
<p float="left">
  <img src="https://github.com/mr3y-the-programmer/Ludi/assets/26522145/3182302b-8b1f-4913-8988-c1f097192c4d" width="33%" />
  <img src="https://github.com/mr3y-the-programmer/Ludi/assets/26522145/21f2b84b-a973-4d13-9513-32c70d96b0cc" width="33%" />
  <img src="https://github.com/mr3y-the-programmer/Ludi/assets/26522145/06340da5-2081-4129-8d07-da11bb90b3df" width="33%" />
</p>

## Features

- Discover trending, top rated, and other highly recommended games.
- Search for a specific game or Filter games by store, tag or platform.
- RSS news reader for your favorite gaming websites.
- Offline support/Caching for RSS feed articles. 
- Full-text search for RSS feed articles.
- Get Updated with the latest deals on games prices & giveaways.
- Adaptive layout design for (Mobile, tablet or Desktop).
- Dark Theme.
- Material 3 design language.

## Download
<a href="https://play.google.com/store/apps/details?id=com.mr3y.ludi" target="_blank">
<img src="https://play.google.com/intl/en_gb/badges/static/images/badges/en_badge_web_generic.png" width=240 />
</a>

Or from [github releases](https://github.com/mr3y-the-programmer/Ludi/releases)

## Tech Stack
[Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html) for sharing code between different platforms.

[Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform) for building shared UI.

[Coil 3](https://github.com/coil-kt/coil) for fetching & displaying images.

[kmpalette](https://github.com/jordond/kmpalette) for generating color palettes from images.

[Voyager](https://github.com/adrielcafe/voyager), [Molecule](https://github.com/cashapp/molecule) for navigation, presenters.

[Ktor Client](https://github.com/ktorio/ktor) for network requests.

[Kotlinx serializtion](https://github.com/Kotlin/kotlinx.serialization) serializing json responses.

[RSS parser](https://github.com/prof18/RSS-Parser) parsing RSS feed.

[Paging 3](https://github.com/cashapp/multiplatform-paging) Loading data from network/database in chunks/pages.

[Datastore proto/preferences](https://developer.android.com/jetpack/androidx/releases/datastore) for saving user preferences.

[kotlin-inject](https://github.com/evant/kotlin-inject) Multiplatform DI.

[Sqldelight](https://github.com/cashapp/sqldelight) for Offline caching.

[Lyricist](https://github.com/adrielcafe/lyricist) type-safe dynamically updated string resources.

[Crashlytics](https://firebase.google.com/docs/crashlytics), [Bugsnag](https://docs.bugsnag.com/platforms/java/other/) for Crash reporting.

[Turbine](https://github.com/cashapp/turbine), [Robolectric](https://github.com/robolectric/robolectric) for testing.

[Refresh Versions](https://github.com/Splitties/refreshVersions) for fetching dependency versions updates.

[App Versioning](https://github.com/ReactiveCircus/app-versioning), [Github actions](https://github.com/mr3y-the-programmer/Ludi/tree/main/.github/workflows), 
[Gradle Play publisher](https://github.com/Triple-T/gradle-play-publisher) for an automated app building & deployment. 

## Contributing
See [CONTRIBUTING.md](https://github.com/mr3y-the-programmer/Ludi/blob/main/CONTRIBUTING.md).

## Todo
- [ ] Write Screenshot tests.
- [ ] Maybe split the shared module into multiple features/modules.
## Credits
- Thanks to [RAWG API](https://rawg.io/apidocs) (Video Games Database).
- Thanks to [Cheapshark API](https://apidocs.cheapshark.com/) (Price comparison website for digital games).
- Thanks to [GamerPower API](https://www.gamerpower.com/api-read) (Providing access to giveaways updated daily).
## License
```
Copyright [2023] [MR3Y]

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
