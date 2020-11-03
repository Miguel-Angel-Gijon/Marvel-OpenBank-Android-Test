package com.example.marvel_openbank

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.example.marvel_openbank.data.entities.Character
import com.example.marvel_openbank.data.local.AppDatabase
import com.example.marvel_openbank.data.local.CharacterDao
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DataBaseReadWriteTest {

    lateinit var characterDao: CharacterDao
    lateinit var appDatabase: AppDatabase

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var mockCharacter: Character

    @Mock
    lateinit var mockCharacter2: Character


    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val context = InstrumentationRegistry.getInstrumentation().context
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        characterDao = appDatabase.characterDao()

        Mockito.`when`(mockCharacter.id).thenReturn(1)
        Mockito.`when`(mockCharacter.description).thenReturn("description")
        Mockito.`when`(mockCharacter.name).thenReturn("name")
        Mockito.`when`(mockCharacter.extension).thenReturn("extension")
        Mockito.`when`(mockCharacter.path).thenReturn("path")


        Mockito.`when`(mockCharacter2.id).thenReturn(2)
        Mockito.`when`(mockCharacter2.description).thenReturn("description")
        Mockito.`when`(mockCharacter2.name).thenReturn("name")
        Mockito.`when`(mockCharacter2.extension).thenReturn("extension")
        Mockito.`when`(mockCharacter2.path).thenReturn("path")

    }

    @After
    fun tearDown() {
        appDatabase.close()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testInsertedAndRetrievedCharacters() {
        runBlocking {
            launch(Dispatchers.IO) {
                val characterList = listOf(mockCharacter, mockCharacter2)
                characterDao.insertAll(characterList)

                val allCharacters = characterDao.getAllCharacters()

                allCharacters.observeForever { }

                assertTrue(allCharacters.value?.size == 2)
                assertTrue(characterList.findLast { mock -> mock.id == allCharacters.value?.get(0)?.id }?.name ==
                        mockCharacter.name)
                assertTrue(characterList.findLast { mock -> mock.id == allCharacters.value?.get(1)?.id }?.description ==
                        mockCharacter2.description)
            }
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testInsertedAndRetrievedACharacter() {
        runBlocking {
            launch(Dispatchers.IO) {
                val character = mockCharacter

                characterDao.insert(character)

                val characterSearch = characterDao.getCharacter(mockCharacter.id)

                characterSearch.observeForever {}

                assertEquals(mockCharacter.id, characterSearch.value?.id)
                assertEquals(mockCharacter.name, characterSearch.value?.name)
                assertEquals(mockCharacter.path, characterSearch.value?.path)
                assertEquals(mockCharacter.description, characterSearch.value?.description)
                assertEquals(mockCharacter.extension, characterSearch.value?.extension)
            }
        }
    }

}