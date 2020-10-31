package com.example.marvel_openbank.utils

import com.example.marvel_openbank.data.entities.Character
import com.example.marvel_openbank.data.entities.CharacterList
import com.example.marvel_openbank.data.entities.CharacterResponse

class TransformersDao {

    fun transformList(characterList : CharacterList): List<Character> {
        val transformList = mutableListOf<Character>()
        characterList.results.map {
            transformList.add(
                transformCharacter(it)
            )
        }
        return transformList
    }

    fun transformCharacter(characterResponse: CharacterResponse): Character =
        Character(
            characterResponse.id,
            characterResponse.description,
            characterResponse.name,
            characterResponse.thumbnail?.path ?: "",
            characterResponse.thumbnail?.extension ?: ""
        )

}