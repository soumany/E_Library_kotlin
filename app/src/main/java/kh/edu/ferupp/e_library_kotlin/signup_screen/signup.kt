package edu.rupp.firstite.buttomNavigationBar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import edu.rupp.firstite.author_screen.AuthorFragment
import edu.rupp.firstite.cart_screen.CartFragment
import edu.rupp.firstite.Home_screen.HomeFragment
import edu.rupp.firstite.R
import edu.rupp.firstite.search_screen.SearchFragment
import edu.rupp.firstite.logOut_screen.LogoutFragment

class MainActivityHomeScreen : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_home_screen)

        bottomNavigationView = findViewById(R.id.bottomNaviView)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navHome -> loadFragment(HomeFragment(), false)
                R.id.navCart -> loadFragment(CartFragment(), false)
                R.id.navSearch -> loadFragment(AuthorFragment(), false)
                R.id.navNotification -> loadFragment(SearchFragment(), false)
                else -> loadFragment(LogoutFragment(), false)
            }
            true
        }

        loadFragment(HomeFragment(), true)
    }

    private fun loadFragment(fragment: Fragment, isAppInitialized: Boolean) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        if (isAppInitialized) {
            fragmentTransaction.add(R.id.frameLayout, fragment)
        } else {
            fragmentTransaction.replace(R.id.frameLayout, fragment)
        }
        fragmentTransaction.commitAllowingStateLoss()
    }
}
