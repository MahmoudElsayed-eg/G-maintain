package com.example.g_maintain.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.g_maintain.R
import com.example.g_maintain.util.FactoryToPassApp
import com.google.android.material.appbar.MaterialToolbar

class MainMenuActivity : AppCompatActivity() {
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
//    private var searchView: SearchView? = null
//    private var searchItem: MenuItem? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
//        val viewModel = ViewModelProvider(this,FactoryToPassApp{MainMenuActivityViewModel(application)})[MainMenuActivityViewModel::class.java]
        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        navController = navHostFragment.navController
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setTitle(R.string.app_name)
        NavigationUI.setupWithNavController(toolbar, navController)
        navController.addOnDestinationChangedListener { _, _, _ ->

            if (navController.graph.startDestination != navController.currentDestination?.id) {
                toolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_back)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }


}