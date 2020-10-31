package com.example.marvel_openbank.ui.characters

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.marvel_openbank.data.repository.CharacterRepository
import com.example.marvel_openbank.utils.Resource
import com.example.marvel_openbank.data.entities.Character

class CharactersViewModel @ViewModelInject constructor(
    private val repository: CharacterRepository
) : ViewModel() {

    var characters: LiveData<Resource<List<Character>>> = repository.getCharacters()

    fun loadCharacters(){
        characters = repository.getCharacters(true)
    }
}
