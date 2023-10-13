package com.mac.allomalar.ui.activities

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.mac.allomalar.R
import com.mac.allomalar.databinding.ActivityAllomalarBinding
import com.mac.allomalar.utils.DownloadingService
import com.mac.allomalar.utils.NetworkHelper
import com.mac.allomalar.utils.NetworkStateChangeReceiver
import com.mac.allomalar.view_models.AllomalarViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*

@AndroidEntryPoint
class AllomalarActivity : AppCompatActivity(), NetworkStateChangeReceiver.ConnectivityReceiverListener {
    private lateinit var binding: ActivityAllomalarBinding
    private val viewModel: AllomalarViewModel by viewModels()

    companion object {
        var isFirstTimeToEnterUserFragment = true
        var isAllomasReadFromApi = false
        var isFirstTimeToEnterHomeFragment = true
        var isCenturiesAreWrittenToRoom= false
        var isBooksAreWrittenToRoom= false
        var isSciencesAreWrittenToRoom= false
        var isAllMadrasasWrittenToRoom = false
        var isAllMadrasasAndAllomaWrittenToRoom = false
        var isFirstTimeToEnterMadrasaFragment= true
        var isFirstTimeToEnterScholar2Fragment= true
        var isFirstTimeToEnterBooksFragment= true
        var isFirstTimeToEnterSciencesFragment= true
        var isFirstTimeToEnterFieldInformationFragment= true
        var isdownloaded = false

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllomalarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        setBottomNavGraph()

        registerReceiver(
            NetworkStateChangeReceiver(),
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
        NetworkStateChangeReceiver.connectivityReceiverListener = this
    }

    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(this, R.id.fragmentContainerView).navigateUp()
    }


    private fun startServiceFunction() {
        Intent(this, DownloadingService::class.java).also {
            startService(it)
        }
    }

    private fun setBottomNavGraph() {
        var navController = findNavController(R.id.fragmentContainerView)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        bottomNav.setupWithNavController(navController)
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (isConnected) {
            CoroutineScope(Dispatchers.IO).launch {
                startServiceFunction()
            }
        }
    }
}