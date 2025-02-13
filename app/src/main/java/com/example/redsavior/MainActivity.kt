package com.example.redsavior

import android.content.ClipData
import android.media.RouteListingPreference
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.redsavior.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var navControler : NavController
    private lateinit var appc : AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
//        By removing this extra space on bottom navigation bar is removed


        navControler = findNavController(R.id.fragmentContainerView)
//        appc = AppBarConfiguration(setOf(R.id.homeFragment,R.id.marketFragment,R.id.portfolioFragment,R.id.newsFragment,R.id.moreFragment))
//
//        setupActionBarWithNavController(navControler,appc)

        binding.bottomNavigationBar.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home_navigation_btn ->navControler.navigate(R.id.homeFragment)
                R.id.market_navigation_btn ->navControler.navigate(R.id.marketFragment)
                R.id.portfolio_navigation_btn ->navControler.navigate(R.id.portfolioFragment)
                R.id.news_navigation_btn ->navControler.navigate(R.id.newsFragment)
                R.id.more_navigation_btn ->navControler.navigate(R.id.moreFragment)
            }
            return@setOnItemSelectedListener true
        }

        navControler.addOnDestinationChangedListener{_ ,destination,_ ->
            if(destination.id == R.id.loginFragment || destination.id == R.id.createFragment || destination.id == R.id.detailsFragment) {
                binding.bottomNavigationBar.visibility = View.GONE
                binding.topAppBar.visibility = View.GONE
            }else{
                binding.bottomNavigationBar.visibility = View.VISIBLE
                binding.topAppBar.visibility = View.VISIBLE
            }
        }
        binding.topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.create_btn -> {
                    navControler.navigate(R.id.createFragment)
                    true
                }
                R.id.login_btn -> {
                    navControler.navigate(R.id.loginFragment)
                    true
                }
                else -> false
            }
        }

    }
}