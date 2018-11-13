package com.tads.graincontrol.graincontrol

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View

import com.tads.graincontrol.graincontrol.activity.AboutActivity
import com.tads.graincontrol.graincontrol.fragment.FragmentChart
import com.tads.graincontrol.graincontrol.fragment.FragmentSilo

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private var fragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadFragment(FragmentSilo())

        val navigation = findViewById<BottomNavigationView>(R.id.navigation)
        navigation.setOnNavigationItemSelectedListener(this)
    }

    private fun loadFragment(fragment: Fragment?): Boolean {
        //switching fragment
        if (fragment != null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit()
            return true
        }
        return false
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.navigation_home -> fragment = FragmentSilo()

            R.id.navigation_dashboard -> fragment = FragmentChart()
        }

        return loadFragment(fragment)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.about, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.about_app -> {
                val intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return false
    }
}
