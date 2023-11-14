package com.example.bookstore.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.bookstore.R
import com.example.bookstore.common.gone
import com.example.bookstore.common.viewBinding
import com.example.bookstore.common.visible
import com.example.bookstore.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.HOME -> navController.navigate(R.id.homeFragment2)
                R.id.SEARCH -> navController.navigate(R.id.searchFragment2)
                R.id.FAV -> navController.navigate(R.id.favoritesFragment)
                R.id.CART -> navController.navigate(R.id.cartFragment)
            }
            true
        }

        navHostFragment.navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment2,
                R.id.favoritesFragment,
                R.id.cartFragment,
                R.id.searchFragment2 -> binding.bottomNav.visible()

                else ->
                    binding.bottomNav.gone()
            }
        }
    }
}