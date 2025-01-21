package  kh.edu.ferupp.e_library_kotlin.ButtonNavigationBar_Screen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import kh.edu.ferupp.e_library_kotlin.Author_Screen.AuthorFragment
import kh.edu.ferupp.e_library_kotlin.Cart_Screen.CartFragment
import kh.edu.ferupp.e_library_kotlin.Home_Screen.HomeFragment
import kh.edu.ferupp.e_library_kotlin.Search_Screen.SearchFragment
import kh.edu.ferupp.e_library_kotlin.Setting_Screen.SettingFragment
import kh.edu.ferupp.e_library_kotlin.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivityButtomnavScreen : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var frameLayout: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_buttomnav_screen)

        bottomNavigationView = findViewById(R.id.bottomNaviView)
        frameLayout = findViewById(R.id.frameLayout)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navHome -> loadFragment(HomeFragment(), false)
                R.id.navCart -> loadFragment(CartFragment(), false)
                R.id.navSearch -> loadFragment(AuthorFragment(), false)
                R.id.navNotification -> loadFragment(SettingFragment(), false)
                else -> loadFragment(SearchFragment(), false)
            }
            true
        }

        loadFragment(HomeFragment(), true)
    }

    private fun loadFragment(fragment: Fragment, isAppInitialized: Boolean) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

        if (isAppInitialized) {
            fragmentTransaction.add(R.id.frameLayout, fragment)
        } else {
            fragmentTransaction.replace(R.id.frameLayout, fragment)
        }
        fragmentTransaction.commitAllowingStateLoss()
    }
}
