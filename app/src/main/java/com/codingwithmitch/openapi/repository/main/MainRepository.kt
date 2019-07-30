package com.codingwithmitch.openapi.repository.main

import android.content.SharedPreferences
import com.codingwithmitch.openapi.persistence.AccountPropertiesDao
import com.codingwithmitch.openapi.persistence.AuthTokenDao
import javax.inject.Inject

class MainRepository
@Inject
constructor(
    val authTokenDao: AuthTokenDao,
    val accountPropertiesDao: AccountPropertiesDao,
    val sharedPrefsEditor: SharedPreferences.Editor
    )
{
    private val TAG: String = "AppDebug"


    suspend fun logout(pk: Int): Int{
        return authTokenDao.updateToken(pk)
    }
}



















