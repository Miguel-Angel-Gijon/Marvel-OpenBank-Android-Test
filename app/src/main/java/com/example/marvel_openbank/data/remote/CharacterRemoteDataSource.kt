package com.example.marvel_openbank.data.remote

import com.example.marvel_openbank.utils.API_KEY
import com.example.marvel_openbank.utils.MD5
import javax.inject.Inject

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