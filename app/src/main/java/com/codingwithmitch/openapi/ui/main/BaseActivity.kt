package com.codingwithmitch.openapi.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.codingwithmitch.openapi.session.SessionManager
import com.codingwithmitch.openapi.ui.auth.AuthActivity
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


abstract class BaseActivity : DaggerAppCompatActivity() {

    val TAG: String = "AppDebug"

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        subscribeObervers()
    }

    fun subscribeObervers(){
        sessionManager.observeAuthState().observe(this, Observer {
            if(it.authToken == null){
                navAuthActivity()
                finish()
            }
        })
    }

    fun logout(){
        sessionManager.logout()
    }

    private fun navAuthActivity(){
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
        finish()
    }


}
















