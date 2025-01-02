package com.example.teacherassistant

import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.teacherassistant.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Set the padding of the root view to the system insets
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemInsets = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )
            val topInset = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
                        or WindowInsetsCompat.Type.displayCutout()
            )
            v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                leftMargin = systemInsets.left
                bottomMargin = systemInsets.bottom
                rightMargin = systemInsets.right
            }
            v.updatePadding(
                top = topInset.top,
            )
            WindowInsetsCompat.CONSUMED
        }

        // Setup app bar
        val appBar = binding.appbar
        setSupportActionBar(appBar)
        supportActionBar?.title = "Classes"

        // Setup bottom navigation
        val navView: BottomNavigationView = binding.navView
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_main) as NavHostFragment
        val navController = navHostFragment.navController
        navView.setupWithNavController(navController)

        // Setup navigation with appBar
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_classes, R.id.navigation_students, R.id.navigation_settings
            )
        )
        appBar.setupWithNavController(navController, appBarConfiguration)

        // Set theme
        val preferences = getSharedPreferences("settings", 0)
        val theme = preferences.getInt("theme", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        AppCompatDelegate.setDefaultNightMode(theme)
    }
}