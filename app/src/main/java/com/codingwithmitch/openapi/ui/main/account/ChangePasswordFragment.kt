package com.codingwithmitch.openapi.ui.main.account


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.codingwithmitch.openapi.R
import com.codingwithmitch.openapi.ui.main.BaseFragment


class ChangePasswordFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_password, container, false)
    }

}
