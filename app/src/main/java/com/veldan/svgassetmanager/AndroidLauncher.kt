package com.veldan.svgassetmanager

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.badlogic.gdx.backends.android.AndroidFragmentApplication
import com.veldan.svgassetmanager.databinding.ActivityMainBinding

lateinit var activityContext: Activity private set
lateinit var navController: NavController private set

class AndroidLauncher : FragmentActivity(), AndroidFragmentApplication.Callbacks {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activityContext = this
        navController = findNavController(R.id.nav_host_fragment)

    }

    override fun exit() {
        finish()
    }

}