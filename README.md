# Jetpack-Compose-Video-Games-Example :video_game:

This is a simple video games discovery app showcasing UI using Jetpack Compose and also tests for composable UI.

This application gets all it's data from [RAWG API][rawg].

## Building the project :hammer: :wrench:

To successfully build this application you need an API key from [RAWG][rawg].

Head on over there and follow the steps to get your key. It's completely free!

Once you get your API key, edit your `local.properties` file and add the key as follows:

`api.key=YOUR_API_KEY`

## Different components used in the project :musical_score: :book: :sparkles:

 - [Clean Architecture][architecture] This app is structured using single module clean architecture
 
 - [MVI Orbit][orbit] The presentation layer uses mvi pattern
 
 - [Jetpack Compose][compose] For creating beautiful screens
 
 - [Compose navigation][navigation] For navigation in the app

 - [Paging 3][paging] For achieving pagination
 
 - [ViewModel][viewmodel] The presentation layer uses the viewmodel pattern
 
 - [Hilt][hilt] For dependency injection
 
 - [Material Icons][icons] For icons displayed in app screens
 
 - [Coil][coil] For displaying images
 
 - [ExoPlater][player] For playing videos

 - [Coroutines][coroutines] For handling async work
 
 - [Retrofit][retrofit] For networking tasks
 
 - [MockWebServer][mockserver] For API/network testing
 
 - [Mockk] For mocking dependencies during testing
 

[rawg]: https://rawg.io/apidocs
[architecture]: https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html
[orbit]: https://github.com/orbit-mvi/orbit-mvi
[compose]: https://developer.android.com/jetpack/compose?gclid=CjwKCAjwyIKJBhBPEiwAu7zll9bjLDRqSH7XtNL-G0txRAeT_QLCcws-_VYPI9Ea-cxFzEC69YbslxoC6BEQAvD_BwE&gclsrc=aw.ds
[navigation]: https://developer.android.com/jetpack/compose/navigation
[paging]: https://developer.android.com/jetpack/compose/lists#large-datasets
[viewmodel]: https://developer.android.com/topic/libraries/architecture/viewmodel
[hilt]: https://developer.android.com/training/dependency-injection/hilt-android
[icons]: https://fonts.google.com/icons?selected=Material+Icons
[coil]: https://coil-kt.github.io/coil/compose/
[player]: https://developer.android.com/guide/topics/media/exoplayer
[coroutines]: https://developer.android.com/kotlin/coroutines
[retrofit]: https://square.github.io/retrofit/
[mockserver]: https://github.com/square/okhttp/tree/master/mockwebserver
[mockk]: https://mockk.io/ANDROID.html
