package com.example.marvel_openbank.ui.characterdetail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.example.marvel_openbank.data.entities.Character
import com.example.marvel_openbank.data.repository.CharacterRepository
import com.example.marvel_openbank.utils.Resource

class CharacterDetailViewModel @ViewModelInject constructor(
    val repository: CharacterRepository
) : ViewModel() {

    val _id = MutableLiveData<Int>()

    private val _character = _id.switchMap { id ->
        repository.getCharacter(id)
    }
    val character: LiveData<Resource<Character>> = _character


    fun start(id: Int) {
        _id.value = id
    }

}
