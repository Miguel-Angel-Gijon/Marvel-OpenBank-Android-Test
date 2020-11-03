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

The logic begins with the viewmodel classes declared in their corresponding fragments. These viewmodels are injected with the [CharacterRepository.kt] class, which is the one that contains the corresponding functions for obtaining data, both remotely and locally, and for sending the database to be updated.

```sh
 class CharacterRepository @Inject constructor(
    private val remoteDataSource: CharacterRemoteDataSource,
    private val localDataSource: CharacterDao
) {

    private val transformersDao: TransformersDao = TransformersDao()

    fun getCharacter(id: Int) = performGetOperation(
        databaseQuery = { localDataSource.getCharacter(id) },
        networkCall = { remoteDataSource.getCharacter(id) },
        saveCallResult = {
            localDataSource.insert(transformersDao.transformCharacter(it.data.results[0]))
        }
    )

    fun getCharacters() = performGetOperation(
        databaseQuery = { localDataSource.getAllCharacters() },
        networkCall = { remoteDataSource.getCharacters() },
        saveCallResult = {
            localDataSource.insertAll(transformersDao.transformList(it.data))
        }
    )

    suspend fun getMoreCharacters(size: Int): Resource<List<Character>>? {
        val remoteSource = remoteDataSource.getCharacters(size).data?.data?.let {
            localDataSource.insertAll(transformersDao.transformList(it))
            Resource.successUpdate(transformersDao.transformList(it))
        }
        return remoteSource ?: Resource.error(Resources.getSystem().getString(R.string.not_get_more_items), null
        )
    }
}
```
As we can see, the getCharacter and getCharacters functions receive an object from a function called performGetOperation found in the [DataAccessStrategy.kt] class, which is responsible for maintaining a strategy as follows:

```sh
fun <T, A> performGetOperation(
    databaseQuery: () -> LiveData<T>,
    networkCall: suspend () -> Resource<A>,
    saveCallResult: suspend (A) -> Unit
): LiveData<Resource<T>> =
    liveData(Dispatchers.IO) {
        emit(Resource.loading())
        val source = databaseQuery.invoke().map { Resource.success(it) }
        emitSource(source)
        val responseStatus = networkCall.invoke()
        when (responseStatus.status) {
            SUCCESS -> {
                saveCallResult(responseStatus.data!!)
            }
            ERROR -> {
                emit(Resource.error(responseStatus.message!!))
                emitSource(source)
            }
            LOADING -> {
            }
        }
    }
```

Through a coroutine livedata and in a different thread from the main one, it will obtain and load the data that exists in the database and send them to be the ones obtained, but on the other hand it will try to make a call to the Api and said The call will return its own state, that if it is SUCCESS it will call the function that we have passed to it as in charge of storing the data and if it is ERROR it will return the data from the database previously obtained.

```sh
data class Resource<out T>(val status: Status, val data: T?, val message: String?) {

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING,
        SUCCESS_UPDATE
    }

    companion object {
        fun <T> success(data: T): Resource<T> {
            return Resource(SUCCESS, data, null)
        }

        fun <T> successUpdate(data: T): Resource<T> {
            return Resource(SUCCESS_UPDATE, data, null)
        }

        fun <T> error(message: String, data: T? = null): Resource<T> {
            return Resource(ERROR, data, message)
        }

        fun <T> loading(data: T? = null): Resource<T> {
            return Resource(LOADING, data, null)
        }
    }
}
```

The response of the coroutine is of type Live <Resource<T>>, and it is this Resource class that wraps these responses that can be of various types depending on the state of the same, so later we can observe the LiveData object that is in our corresponding viewmodel from the fragment.

```
        viewModel.characters.observe(viewLifecycleOwner, Observer {
            with(binding) {
                when (it.status) {
                    SUCCESS -> {
                        progressBar.visibility = GONE
                        if (!it.data.isNullOrEmpty()) adapterCharacters.setItems(ArrayList(it.data))
                    }
                    ERROR -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        progressBar.visibility = GONE
                        progressBarMoreItems.visibility = GONE
                    }
                    SUCCESS_UPDATE -> {
                        progressBarMoreItems.visibility = GONE
                        if (!it.data.isNullOrEmpty())
                            adapterCharacters.updateItems(it.data)
                        closeSearch()
                    }
                    LOADING ->
                        progressBar.visibility = VISIBLE
                }
            }
        })
```

We can know if we have to report an error, reload the adapter or show or hide the loading.
With this same system as our list, it is loaded from 20 to 20 items remotely so as not to stop the operation of the app and maintain a pleasant user experience; From the fragment of [CharactersFragment.kt] we control when our list reaches the end, to re-launch the call to the viewmodel and make it redo the data update process from the server but with an "offset" that is controlled from the call and it will be the total of elements that we already have.

For the calls to the Api we have a bridge class between the retrofit call model and our viewmodel [CharacterRemoteDataSource.kt]. This class provides us with two getters, one for the list and one for a specific character.

```
class CharacterRemoteDataSource @Inject constructor(
    private val characterService: CharacterService
) : BaseDataSource() {

    suspend fun getCharacters(offset : Int = 0) = getResult {
        val md5 = MD5()
        md5.getHashComplete()
        val hash: String = md5.hash ?: ""
        characterService.getAllCharacters("name", offset, md5.timestamp, API_KEY, hash)
    }

    suspend fun getCharacter(id: Int) = getResult {
        val md5 = MD5()
        md5.getHashComplete()
        val hash: String = md5.hash ?: ""
        characterService.getCharacter(id, md5.timestamp, API_KEY, hash)
    }
}
```

The peculiarity of the marvel API for developers that as such, I had to create an account to use it. This account provides us with a PRIVATE KEY and an API KEY, which in combination with a TIMESTAMP of the moment of the call, we will obtain a unique hash that will give us access to the calls and their corresponding responses. This hash is generated with the MD5 utility class.

```
class MD5() {

    lateinit var timestamp: String
    var hash: String? = null

    lateinit var apikey: String


    private fun getMD5EncryptedString(encTarget: String): String? {
        var mdEnc: MessageDigest? = null
        try {
            mdEnc = MessageDigest.getInstance("MD5")
        } catch (e: NoSuchAlgorithmException) {
            println("Exception while encrypting to md5")
            e.printStackTrace()
        } // Encryption algorithm
        mdEnc?.update(encTarget.toByteArray(), 0, encTarget.length)
        var md5: String = BigInteger(1, mdEnc?.digest()).toString(16)
        while (md5.length < 32) {
            md5 = "0$md5"
        }
        return md5
    }

    fun getHashComplete() {
        timestamp = Timestamp(System.currentTimeMillis()).time.toString()
        hash = getMD5EncryptedString("$timestamp$PRIVATE_KEY$API_KEY")
    }
    
}
```

So far all the explanation.
I leave a link from TalentoMobile, partner of said development.

[//]: # (These are reference links used in the body of this note and get stripped out when the markdown processor does its job. There is no need to format nicely because it shouldn't be seen. Thanks SO - http://stackoverflow.com/questions/4823468/store-comments-in-markdown-syntax)


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
