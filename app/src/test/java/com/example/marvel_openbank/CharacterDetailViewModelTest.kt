package com.example.marvel_openbank

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.marvel_openbank.data.entities.CharacterResponse
import com.example.marvel_openbank.data.repository.CharacterRepository
import com.example.marvel_openbank.ui.characterdetail.CharacterDetailViewModel
import com.example.marvel_openbank.utils.Resource
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations


@RunWith(JUnit4::class)
class CharacterDetailViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    fun <T> anyObject(): T = Mockito.any()

    @Mock
    lateinit var mockCharacterRepository: CharacterRepository

    @Mock
    lateinit var mockObserver: Observer<Int>

    @Mock
    private lateinit var mockCharacterResponse: LiveData<Resource<CharacterResponse>>


    lateinit var viewModel: CharacterDetailViewModel


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        this.viewModel = CharacterDetailViewModel(this.mockCharacterRepository)
    }

    @Test
    fun run_start_asign_id(){
        viewModel.start(1)
        Assert.assertNotNull(viewModel._id.value)
        Assert.assertEquals(1, viewModel._id.value)
    }

    @Test
    fun create_viewmodel(){
        `when`(viewModel.repository.getCharacter(ArgumentMatchers.anyInt())).thenAnswer {
            invocation -> (invocation.arguments[0] as (LiveData<Resource<CharacterResponse>>) -> Unit).invoke(mockCharacterResponse)
        }
    }
}