package com.codingwithmitch.openapi.ui.main

import android.os.Bundle
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigation.*
import androidx.navigation.ui.NavigationUI.*
import com.codingwithmitch.openapi.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : BaseActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener,
    NavController.OnDestinationChangedListener
{
    lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            initNavigation()
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        initNavigation()
    }

    private fun initNavigation() {
        bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        val navController = findNavController(this, R.id.main_nav_host_fragment)
        setupWithNavController(bottomNavigationView, navController)
        bottomNavigationView.setOnNavigationItemSelectedListener(this)
        navController.addOnDestinationChangedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.nav_home -> navMainFragment()

            R.id.nav_create_blog -> navCreateBlogFragment()

            R.id.nav_account -> navAccountFragment()
        }

        item.setChecked(true)
        return true
    }

    private fun navMainFragment(){
        // clear back-stack when moving back to MainFragment
        val navOptions: NavOptions = NavOptions.Builder()
            .setPopUpTo(R.id.main_nav_graph, true)
            .build()
        findNavController(this, R.id.main_nav_host_fragment).navigate(R.id.blogFragment,null, navOptions)
    }

    private fun navCreateBlogFragment(){
        findNavController(this, R.id.main_nav_host_fragment).navigate(R.id.createBlogFragment)
    }

    private fun navAccountFragment(){
        findNavController(this, R.id.main_nav_host_fragment).navigate(R.id.accountFragment)
    }

    // Correct icon highlighting
    override fun onDestinationChanged(controller: NavController, destination: NavDestination, arguments: Bundle?) {
        when(destination.label){
            getString(R.string.fragment_account) -> bottomNavigationView.menu.findItem(R.id.nav_account).setChecked(true)

            getString(R.string.fragment_create_blog) -> bottomNavigationView.menu.findItem(R.id.nav_create_blog).setChecked(true)

            getString(R.string.fragment_update_blog) -> bottomNavigationView.menu.findItem(R.id.nav_home).setChecked(true)
            getString(R.string.fragment_blog) -> bottomNavigationView.menu.findItem(R.id.nav_home).setChecked(true)
            getString(R.string.fragment_view_blog) -> bottomNavigationView.menu.findItem(R.id.nav_home).setChecked(true)
        }
    }
}




















