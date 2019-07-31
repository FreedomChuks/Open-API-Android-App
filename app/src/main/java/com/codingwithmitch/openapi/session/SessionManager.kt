package com.codingwithmitch.openapi.session

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.codingwithmitch.openapi.persistence.AuthTokenDao
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import javax.inject.Inject

class SessionManager @Inject constructor(val authTokenDao: AuthTokenDao) {

    private val TAG: String = "AppDebug";

    private val cachedToken = MutableLiveData<SessionResource>()

    fun observeAuthState(): LiveData<SessionResource>{
        return cachedToken
    }

    fun logout(){
        Log.d(TAG, "logout: ")
        CoroutineScope(IO).launch{
            try{
                cachedToken.value!!.authToken!!.account_pk?.let {
                    authTokenDao.nullifyToken(it)
                } ?: throw CancellationException("Token Error. Logging out user.")
            }catch (e: CancellationException) {
                Log.e(TAG, "logout: ${e.message}")
            }
            catch (e: Exception) {
                Log.e(TAG, "logout: ${e.message}")
            }
            finally {
                setValue(SessionResource(null, null))
            }
        }
    }

    fun onReturnSessionError(errorMessage: String){
        setValue(SessionResource(null, errorMessage))
    }

    fun login(newValue: SessionResource){
        setValue(newValue)
    }

    fun setValue(newValue: SessionResource) {
        GlobalScope.launch(Main) {
            if (cachedToken.value != newValue) {
                cachedToken.value = newValue
            }
        }
    }
}
























