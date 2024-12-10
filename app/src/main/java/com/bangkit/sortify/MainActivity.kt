package com.bangkit.sortify

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.bangkit.sortify.databinding.ActivityLoginBinding
import com.bangkit.sortify.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadFragment(HomeFragment())
        binding.bottomNav as BottomNavigationView
        binding.bottomNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.menu_home -> {
                    binding.bottomNav.setBackgroundColor(resources.getColor(R.color.green))
                    loadFragment(HomeFragment())
                    true
                }
                R.id.menu_history -> {
                    binding.bottomNav.setBackgroundColor(resources.getColor(R.color.orange))
                    loadFragment(HistoryFragment())
                    true
                }

                else -> {
                    true
                }
            }
        }
    }

    private fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.commit()
    }
}