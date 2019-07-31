package com.codingwithmitch.openapi

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.codingwithmitch.openapi.di.main.MainScope
import com.codingwithmitch.openapi.models.AuthToken
import com.codingwithmitch.openapi.persistence.AuthTokenDao
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import javax.inject.Inject

@MainScope
class SessionManager @Inject constructor(val authTokenDao: AuthTokenDao) {

    private val TAG: String = "AppDebug";

    private val cachedToken = MutableLiveData<AuthToken>()

    fun observeAuthState(): LiveData<AuthToken>{
        return cachedToken
    }

    fun logout(){
        Log.d(TAG, "logout: ")
        CoroutineScope(IO).launch{
            val logoutResult: Deferred<Int> = async {
                cachedToken.value?.let{
                    it.account_pk?.let {
                            it1 -> authTokenDao.nullifyToken(it1)
                    }?: TODO("Return Logout Error to UI")
                }?: TODO("Return Logout Error to UI")
            }
            logoutResult.await()
            setValue(AuthToken(-1, null))
        }
    }

    fun login(newValue: AuthToken){
        setValue(newValue)
    }

    fun setValue(newValue: AuthToken) {
        GlobalScope.launch(Main) {
            if (cachedToken.value != newValue) {
                cachedToken.value = newValue
            }
        }
    }

}
























