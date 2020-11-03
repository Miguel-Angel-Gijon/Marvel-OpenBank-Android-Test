# Marvel OpenBank Android Test

[![N|Solid](https://upload.wikimedia.org/wikipedia/commons/thumb/0/04/MarvelLogo.svg/800px-MarvelLogo.svg.png)](https://developer.marvel.com/)

Android app that shows a list of Marvel comic characters obtained from the web https://developer.marvel.com/.

## Functioning

Upon accessing the app, it will show us a loading presentation with the Marvel company logo and after a few seconds we will access a list with a heading named "Characters Marvel".

#### Characters Marvel screen

In the header we also have an icon with a magnifying glass that allows us to discover a text bar to make a search within the list.
The list shows a round image for each character, their name on the right and an arrow pointing to the right that indicates more information.
The search is by content and is activated dynamically, so as we write we can see the results live.
Another feature of this first screen is that as we go down the list we will load more items that will be stored for the next time we open the app.

#### Character Detail screen

On this screen we have a header with the title "Character Detail" and an arrow to return to the previous screen. In the rest of the screen we will see the name of the character above an image that will load if it is not already, under said image the last time that character appeared in a comic and a description of it below.

## Technological Functionality

The app is developed in Kotlin language, using the MVVM pattern.

##### The project is structured as follows:
//////////////////////////////////////////

---> Data Folder (It contains everything related to the data and its obtaining)

--------> Entities Folder (Model for db and network)

--------> Local Folder (Tools for configure and use db)

-------> Remote Folder (Tools for calls to network)

-------> Repository Folder (Bridge between local and remote use)

---> Di Folder (It contains the configuration for dependency injection)

---> Ui Folder (It contains the activity main, fragments, adaptors and viewmodels)

-------> CharacterDetail Folder (For screen Character detail)

-------> Character Folder (For screen Characters Marvel)

---> Utils Folder (It contains generic tools for app)


##### The classes that make up the views section are:
//////////////////////////////////////////
* [CharactersFragment.kt] - Class that represents the Fragment that contains the list of Marvel characters.
* [CharacterDetailFragment.kt] - Class that represents the Fragment that contains the details of the Marvel character.

##### The classes that maintain the logic and communication, that is, the corresponding viewmodels:
//////////////////////////////////////////
* [CharactersViewModel.kt] - ViewModel for CharactersFragment
* [CharacterDetailViewModel.kt] - ViewModel for CharacterDetailFragment

##### For dependency injection using Hilt:
//////////////////////////////////////////
* [AppModule.kt]

##### For database local in Room:
//////////////////////////////////////////
* [AppDatabase.kt]
* [CharacterDao.kt]

##### For calls to Api using Retrofit:
//////////////////////////////////////////
* [BaseDataSource.kt]
* [CharacterRemoteDataSource.kt]
* [CharacterService.kt]

##### For strategy between local and remote source:
//////////////////////////////////////////
* [DataAccessStrategy.kt]

##### Create a bridge to obtain data both locally and remotely:
//////////////////////////////////////////
* [CharacterRepository.kt]


### Dependecies

| Dependency | Implementation |
| ------ | ------ |
| Hilt | implementation 'com.google.dagger:hilt-android:2.28.1-alpha'|
| Hilt | implementation "androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha01"|
| Hilt | kapt 'com.google.dagger:hilt-android-compiler:2.28.1-alpha'|
| Hilt | kapt "androidx.hilt:hilt-compiler:1.0.0-alpha01"|
| Room | implementation "androidx.room:room-runtime:$room_version"|
| Room | implementation "androidx.room:room-ktx:$room_version"|
| Room | kapt "androidx.room:room-compiler:$room_version" |
| Retrofit | implementation 'com.squareup.retrofit2:retrofit:2.9.0'|
| Retrofit | implementation 'com.squareup.retrofit2:converter-gson:2.9.0'|
| Retrofit | implementation 'androidx.work:work-runtime:2.3.4' |
| Navigation | implementation "androidx.navigation:navigation-fragment-ktx:$nav_version"|
| Navigation | implementation "androidx.navigation:navigation-ui-ktx:$nav_version" |
| Glide | implementation 'com.github.bumptech.glide:glide:4.11.0'|
| Glide | kapt 'com.github.bumptech.glide:compiler:4.11.0' |
| Kotlin Coroutines | [plugins/googleanalytics/README.md][PlGa] |
| Lifecycle | implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version"|
| Lifecycle | implementation "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version"|
| Lifecycle | implementation "androidx.lifecycle:lifecycle-common-java8:$lifecycle_version"|
| Lifecycle | implementation 'androidx.lifecycle:lifecycle-extensions:2.2.0' |
| Mockito | testImplementation 'org.mockito:mockito-core:3.1.0' |
| Mockito | testImplementation'org.mockito:mockito-inline:3.1.0'|
| Test/Androidx-Core | testImplementation "androidx.arch.core:core-testing:2.1.0"|
| Annotations | implementation 'com.android.support:support-annotations:28.0.0'|
| Test/Androidx | testImplementation "androidx.test:runner:1.3.0"|
| Espresso | testImplementation "androidx.test.espresso:espresso-core:3.3.0"|
| Test/Likendin | testImplementation 'com.linkedin.dexmaker:dexmaker-mockito:2.2.0'|
| Test | androidTestImplementation "androidx.test:runner:1.3.0"|
| Robolectric | testImplementation "org.robolectric:robolectric:4.3" |


### Functioning



   [CharactersFragment.kt]: <https://github.com/Miguel-Angel-Gijon/Marvel-OpenBank-Android-Test/blob/master/app/src/main/java/com/example/marvel_openbank/ui/characters/CharactersFragment.kt>
   [CharacterDetailFragment.kt]: <https://github.com/Miguel-Angel-Gijon/Marvel-OpenBank-Android-Test/blob/master/app/src/main/java/com/example/marvel_openbank/ui/characterdetail/CharacterDetailFragment.kt>
   [CharactersViewModel.kt]: <https://github.com/Miguel-Angel-Gijon/Marvel-OpenBank-Android-Test/blob/master/app/src/main/java/com/example/marvel_openbank/ui/characters/CharactersViewModel.kt>
   [CharacterDetailViewModel.kt]: <https://github.com/Miguel-Angel-Gijon/Marvel-OpenBank-Android-Test/blob/master/app/src/main/java/com/example/marvel_openbank/ui/characterdetail/CharacterDetailViewModel.kt>
   [AppModule.kt]: <https://github.com/Miguel-Angel-Gijon/Marvel-OpenBank-Android-Test/blob/master/app/src/main/java/com/example/marvel_openbank/di/AppModule.kt>
   [AppDatabase.kt]: <https://github.com/Miguel-Angel-Gijon/Marvel-OpenBank-Android-Test/blob/master/app/src/main/java/com/example/marvel_openbank/data/local/AppModule.kt>
   [CharacterDao.kt]: <https://github.com/Miguel-Angel-Gijon/Marvel-OpenBank-Android-Test/blob/master/app/src/main/java/com/example/marvel_openbank/data/local/CharacterDao.kt>
   [BaseDataSource.kt]: <https://github.com/Miguel-Angel-Gijon/Marvel-OpenBank-Android-Test/blob/master/app/src/main/java/com/example/marvel_openbank/data/remote/BaseDataSource.kt>
   [CharacterRemoteDataSource.kt]:<https://github.com/Miguel-Angel-Gijon/Marvel-OpenBank-Android-Test/blob/master/app/src/main/java/com/example/marvel_openbank/data/remote/CharacterRemoteDataSource.kt>
   [CharacterService.kt]:<https://github.com/Miguel-Angel-Gijon/Marvel-OpenBank-Android-Test/blob/master/app/src/main/java/com/example/marvel_openbank/data/remote/CharacterService.kt>
   [DataAccessStrategy.kt]:<https://github.com/Miguel-Angel-Gijon/Marvel-OpenBank-Android-Test/blob/master/app/src/main/java/com/example/marvel_openbank/utils/DataAccessStrategy.kt>
   [CharacterRepository.kt]:<https://github.com/Miguel-Angel-Gijon/Marvel-OpenBank-Android-Test/blob/master/app/src/main/java/com/example/marvel_openbank/data/repository/CharacterRepository.kt>

   
   [![N|Solid](https://networking.mwcnetwork.es/assets/img/logos/empresas/talent.jpg)](https://www.talentomobile.com)
