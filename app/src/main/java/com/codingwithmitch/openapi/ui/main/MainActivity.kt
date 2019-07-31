package com.codingwithmitch.openapi.ui.main

import android.os.Bundle
import com.codingwithmitch.openapi.BaseActivity
import com.codingwithmitch.openapi.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class MainActivity : BaseActivity() {

    private val TAG: String = "AppDebug"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_logout.setOnClickListener {
            CoroutineScope(IO).launch{
                logout()
            }
        }
    }

}