package com.codingwithmitch.openapi.repository.auth

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.codingwithmitch.openapi.SessionManager
import com.codingwithmitch.openapi.api.auth.OpenApiAuthService
import com.codingwithmitch.openapi.api.auth.network_responses.*
import com.codingwithmitch.openapi.models.AccountProperties
import com.codingwithmitch.openapi.models.AuthToken
import com.codingwithmitch.openapi.persistence.AccountPropertiesDao
import com.codingwithmitch.openapi.persistence.AuthTokenDao
import com.codingwithmitch.openapi.ui.auth.state.AuthState
import com.codingwithmitch.openapi.ui.auth.state.ViewState
import com.codingwithmitch.openapi.util.PreferenceKeys
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

class AuthRepository
@Inject
constructor(
    val authTokenDao: AuthTokenDao,
    val accountPropertiesDao: AccountPropertiesDao,
    val openApiAuthService: OpenApiAuthService,
    val sharedPrefsEditor: SharedPreferences.Editor,
    val sessionManager: SessionManager
    )
{
    private val TAG: String = "AppDebug"

    fun attemptRegistration(email: String, username: String, password: String, confirmPassword: String): LiveData<AuthState> {

        return object: AuthNetworkBoundResource<RegistrationResponse>() {

            override suspend fun createResponse(): Response<RegistrationResponse> {
                return openApiAuthService.register(email,username,password, confirmPassword)
            }

            override fun saveUserToPrefs(email: String) {
                saveAuthenticatedUserToPrefs(email)
            }

            override suspend fun saveTokenLocally(authToken: AuthToken): Long {
                return authTokenDao.insert(authToken)
            }

            override suspend fun saveAccountPropertiesLocally(accountProperties: AccountProperties): Long {
                return accountPropertiesDao.insert(accountProperties)
            }

        }.asLiveData()
    }

    fun attemptLogin(email: String, password: String): LiveData<AuthState> {

        return object: AuthNetworkBoundResource<LoginResponse>() {

            override suspend fun createResponse(): Response<LoginResponse> {
                return openApiAuthService.login(email.toLowerCase(), password)
            }

            override fun saveUserToPrefs(email: String) {
                saveAuthenticatedUserToPrefs(email)
            }

            override suspend fun saveTokenLocally(authToken: AuthToken): Long {
                return authTokenDao.insert(authToken)
            }

            override suspend fun saveAccountPropertiesLocally(accountProperties: AccountProperties): Long {
                return accountPropertiesDao.insert(accountProperties)
            }

        }.asLiveData()
    }

    private val ERROR_RESPONSE = "Error"

    fun login2(email: String, password: String) = liveData{
        emit(ViewState.showProgress())
        try{
            val response: LoginResponse = openApiAuthService.login2(email.toLowerCase(), password)

            if(response.response.equals(ERROR_RESPONSE) ){
                emit(ViewState.showErrorDialog(response.errorMessage))
            }
            else {
                saveAuthenticatedUserToPrefs(response.email)
                try{
                    if(accountPropertiesDao.insert(AccountProperties(response.pk, response.email, "")) > -1){
                        // AccountProperties insert success

                        if(authTokenDao.insert(AuthToken(response.pk, response.token)) > -1){
                            // AccountProperties insert success
                            emit(ViewState.hideProgress())
                            sessionManager.setValue(AuthToken(response.pk, response.token))
                        }
                        else{
                            // insert fail
                            emit(ViewState.showErrorDialog("Error saving authentication token.\nTry restarting the app."))
                        }
                    }

                }catch (e: Exception){
                    Log.e(TAG, "handleLoginResponse: ${e.message}")
                    var errorMessage = e.message
                    if(errorMessage.isNullOrEmpty()){
                        errorMessage = "Unknown error."
                    }
                    emit(ViewState.showErrorDialog(errorMessage))
                }
            }

        }catch (e: Throwable) {
            Log.e(TAG, "attempRegistration: ${e.message}")
            var errorMessage = e.message
            if(errorMessage.isNullOrEmpty()){
                errorMessage = "Unknown error."
            }
            emit(ViewState.showErrorDialog(errorMessage))

        } catch (e: HttpException) {
            Log.e(TAG, "attempRegistration: ${e.message}")
            var errorMessage = e.message
            if(errorMessage.isNullOrEmpty()){
                errorMessage = "Unknown error."
            }
            emit(ViewState.showErrorDialog(errorMessage))
        }
    }

    suspend fun retrieveTokenFromLocalDb(pk: Int): AuthToken? {
        return authTokenDao.searchByPk(pk)
    }

    suspend fun retrieveAccountPropertiesUsingEmail(email: String): AccountProperties?{
        return accountPropertiesDao.searchByEmail(email)
    }


    fun saveAuthenticatedUserToPrefs(email: String){
        sharedPrefsEditor.putString(PreferenceKeys.PREVIOUS_AUTH_USER, email)
        sharedPrefsEditor.apply()
    }

}



















