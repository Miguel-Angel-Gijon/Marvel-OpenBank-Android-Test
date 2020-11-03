package com.example.marvel_openbank

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.example.marvel_openbank.data.entities.Character
import com.example.marvel_openbank.data.repository.CharacterRepository
import com.example.marvel_openbank.ui.characterdetail.CharacterDetailViewModel
import com.example.marvel_openbank.utils.Resource
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.any
import org.mockito.MockitoAnnotations


@RunWith(JUnit4::class)
class CharacterDetailViewModelTest :BaseTest() {

    @Mock
    lateinit var mockCharacterRepository: CharacterRepository

    @Mock
    private lateinit var mockLiveDataResourceCharacter: LiveData<Resource<Character>>


    lateinit var viewModel: CharacterDetailViewModel

    override fun setup() {
        MockitoAnnotations.initMocks(this)
        this.viewModel = CharacterDetailViewModel(this.mockCharacterRepository)
    }

    @Test
    fun run_start_asign_id() {
        viewModel.start(1)
        assertNotNull(viewModel._id.value)
        assertEquals(1, viewModel._id.value)
    }

    @Test
    fun run_start_check_character() {
        `when`(mockCharacterRepository.getCharacter(Mockito.anyInt())).thenReturn(mockLiveDataResourceCharacter)
        viewModel.character.observeForever { }
        viewModel.start(Mockito.anyInt())
        assertNotNull(viewModel.character)
        assertEquals(mockLiveDataResourceCharacter.value, viewModel.character.value)
    }
}

