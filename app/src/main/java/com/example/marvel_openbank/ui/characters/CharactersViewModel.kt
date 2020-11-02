package com.example.marvel_openbank.ui.characters

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marvel_openbank.data.entities.Character
import com.example.marvel_openbank.data.repository.CharacterRepository
import com.example.marvel_openbank.utils.Resource
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class CharactersViewModel @ViewModelInject constructor(
    private val repository: CharacterRepository
) : ViewModel() {

    var characters: LiveData<Resource<List<Character>>> = repository.getCharacters()

    fun loadCharacters() {
        viewModelScope.launch(IO) {
            (characters as MutableLiveData).postValue(
                repository.getMoreCharacters(
                    characters.value?.data?.size ?: 0
                )
            )
        }
    }
}
