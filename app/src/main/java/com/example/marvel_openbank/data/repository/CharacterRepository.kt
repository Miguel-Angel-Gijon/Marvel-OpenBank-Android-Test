package com.example.marvel_openbank.data.repository

import android.content.res.Resources
import com.example.marvel_openbank.R
import com.example.marvel_openbank.data.entities.Character
import com.example.marvel_openbank.data.entities.CharacterListResponse
import com.example.marvel_openbank.data.local.CharacterDao
import com.example.marvel_openbank.data.remote.CharacterRemoteDataSource
import com.example.marvel_openbank.utils.Resource
import com.example.marvel_openbank.utils.TransformersDao
import com.example.marvel_openbank.utils.performGetOperation
import javax.inject.Inject

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