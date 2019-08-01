package com.codingwithmitch.openapi.ui.main.blog


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import com.codingwithmitch.openapi.R
import com.codingwithmitch.openapi.ui.main.BaseFragment
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_blog.*
import kotlinx.android.synthetic.main.fragment_view_blog.*


class ViewBlogFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_blog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        update_blog.setOnClickListener{
            findNavController().navigate(R.id.updateBlogFragment)
        }
    }

}
