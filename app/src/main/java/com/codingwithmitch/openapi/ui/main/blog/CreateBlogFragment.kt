package com.codingwithmitch.openapi.ui.main.blog


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.codingwithmitch.openapi.R
import com.codingwithmitch.openapi.ui.main.BaseFragment


class CreateBlogFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_blog, container, false)
    }

}
