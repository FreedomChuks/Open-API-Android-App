package com.codingwithmitch.openapi.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.afollestad.materialdialogs.MaterialDialog

import com.codingwithmitch.openapi.R
import com.codingwithmitch.openapi.session.SessionManager
import com.codingwithmitch.openapi.models.AuthToken
import com.codingwithmitch.openapi.session.SessionResource
import com.codingwithmitch.openapi.ui.auth.state.AuthScreenState
import com.codingwithmitch.openapi.ui.main.MainActivity
import com.codingwithmitch.openapi.viewmodels.ViewModelProviderFactory
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class AuthActivity : DaggerAppCompatActivity() {

    private val TAG: String = "AppDebug"

    lateinit var progressBar: ProgressBar

    lateinit var viewModel: AuthActivityViewModel

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        progressBar = findViewById(R.id.progress_bar)

        Log.d(TAG, "AuthActivity: started")

        viewModel = ViewModelProviders.of(this, providerFactory).get(AuthActivityViewModel::class.java)

        subscribeObservers()
    }

    fun subscribeObservers(){
        viewModel.observeAuthScreenState().observe(this, Observer { authScreenState ->
            when(authScreenState){
                is AuthScreenState.Loading -> displayProgressBar(true)
                is AuthScreenState.Data -> {
                    displayProgressBar(false)
                    authScreenState.authToken?.let{
                        sessionManager.login(SessionResource(it, null))
                    }
                }
                is AuthScreenState.Error -> {
                    displayProgressBar(false)
                    displayErrorDialog(authScreenState.errorMessage)
                }
            }
        })


        sessionManager.observeAuthState().observe(this, Observer {
            it?.let {
                if(it.authToken?.account_pk != -1 && it.authToken?.token != null){
                    navMainActivity()
                }
                it.errorMessage?.let {
                    displayErrorDialog(errorMessage = it)
                }
            }
        })
    }

    fun navMainActivity(){
        Log.d(TAG, "navMainActivity: called.")
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun displayProgressBar(bool: Boolean){
        if(bool){
            progressBar.visibility = View.VISIBLE
        }
        else{
            progressBar.visibility = View.GONE
        }
    }

    fun displayErrorDialog(errorMessage: String){
        MaterialDialog(this)
            .title(R.string.text_error)
            .message(text = errorMessage){
                lineSpacing(2F)
            }
            .positiveButton(R.string.text_ok)
            .show()
    }

}





















