package com.example.marvel_openbank

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.marvel_openbank.data.entities.Character
import com.example.marvel_openbank.data.repository.CharacterRepository
import com.example.marvel_openbank.ui.characters.CharactersViewModel
import com.example.marvel_openbank.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations


@RunWith(JUnit4::class)
class CharactersViewModelTest : BaseTest() {

    @Mock
    lateinit var mockCharacterRepository: CharacterRepository

    private lateinit var viewModel: CharactersViewModel

    @Mock
    private lateinit var mockLiveDataResourceCharacters: LiveData<Resource<List<Character>>>

    @Mock
    private lateinit var mockLiveDataResourceCharactersLoad: MutableLiveData<Resource<List<Character>>>


    override fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `get characters from repository on init`() {
        Mockito.`when`(mockCharacterRepository.getCharacters())
            .thenReturn(mockLiveDataResourceCharacters)
        this.viewModel = CharactersViewModel(this.mockCharacterRepository)
        viewModel.characters.observeForever { }
        Assert.assertEquals(mockLiveDataResourceCharacters.value, viewModel.characters.value)
    }

    @Test
    fun `get characters from repository on loadCharacters`() {
        Mockito.`when`(mockCharacterRepository.getCharacters())
            .thenReturn(mockLiveDataResourceCharactersLoad)
        this.viewModel = CharactersViewModel(this.mockCharacterRepository)
        runBlocking {
            launch(Dispatchers.IO) {
                viewModel.loadCharacters()
                viewModel.characters.observeForever { }
                Assert.assertEquals(
                    mockLiveDataResourceCharactersLoad.value,
                    viewModel.characters.value
                )
            }
        }
    }

}