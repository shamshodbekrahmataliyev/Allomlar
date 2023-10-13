package com.mac.allomalar.ui.fragments

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import com.mac.allomalar.adapters.PagerAdapter
import com.mac.allomalar.databinding.FragmentHomeBinding
import com.mac.allomalar.models.Status
import com.mac.allomalar.ui.activities.AllomalarActivity
import com.mac.allomalar.ui.activities.AllomalarActivity.Companion.isAllMadrasasWrittenToRoom
import com.mac.allomalar.utils.NetworkHelper
import com.mac.allomalar.utils.NetworkStateChangeReceiver
import com.mac.allomalar.view_models.HomeFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(), NetworkStateChangeReceiver.ConnectivityReceiverListener {

    @Inject
    lateinit var networkHelper: NetworkHelper

    private var a = 0
    private val _go = MutableLiveData<Int>()
    private val viewModel: HomeFragmentViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding
    private lateinit var pagerAdapter: PagerAdapter
    val list = ArrayList<PagerFragment>()
    val job = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + job)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)

        activity?.registerReceiver(
            NetworkStateChangeReceiver(),
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
        NetworkStateChangeReceiver.connectivityReceiverListener = this

        if (!networkHelper.isNetworkConnected()) {
            uiScope.launch {
                setData()
                binding.progressHome.visibility = View.INVISIBLE
                AllomalarActivity.isFirstTimeToEnterHomeFragment = false
            }

        } else if(networkHelper.isNetworkConnected() && !isAllMadrasasWrittenToRoom && !isAllMadrasasWrittenToRoom){
            binding.progressHome.visibility = View.VISIBLE
            binding.vp.visibility = View.INVISIBLE
            startToReadAndWriteToRoom()
        }

        return binding.root
    }


    override fun onResume() {
        super.onResume()
        if (networkHelper.isNetworkConnected() && !AllomalarActivity.isFirstTimeToEnterHomeFragment){
            setData()
        }
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (isConnected && !isAllMadrasasWrittenToRoom && !isAllMadrasasWrittenToRoom) {
            binding.progressHome.visibility = View.VISIBLE
            binding.vp.visibility = View.INVISIBLE
            startToReadAndWriteToRoom()
        }
    }

    private fun startToReadAndWriteToRoom() {

        uiScope.launch {
            viewModel.allMadrasas.observe(viewLifecycleOwner) { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        var job = CoroutineScope(Dispatchers.Main).launch {
                            viewModel.insertAllMadrasa(resource.data)
                        }
                        CoroutineScope(Dispatchers.Main).launch {
                            job.join()
                            a++
                            _go.value = a
                        }
                    }
                }
            }
        }

        uiScope.async {
            viewModel.allCenturies.observe(viewLifecycleOwner) { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        val job = CoroutineScope(Dispatchers.Main).launch {
                            viewModel.insertAllCenturies(resource.data)
                        }

                        CoroutineScope(Dispatchers.Main).launch {
                            job.join()
                            a++
                            _go.value = a
                        }
                    }

                }
            }
        }

        uiScope.async {
            _go.observe(viewLifecycleOwner) {
                if (it == 2) {
                    AllomalarActivity.isCenturiesAreWrittenToRoom = true
                    AllomalarActivity.isAllMadrasasWrittenToRoom = true
                    AllomalarActivity.isFirstTimeToEnterHomeFragment = false
                    setData()
                    binding.progressHome.visibility = View.INVISIBLE
                    binding.vp.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setData() {
        CoroutineScope(Dispatchers.Main).launch {
            val list1 = viewModel.getAllCenturiesFromRoom()
            list.clear()
            list1.forEach {
                val fragment = PagerFragment.getInstance(it)
                list.add(fragment)
            }
            setAdapter()
        }
    }

    private fun setAdapter() {
        val tab = binding.dotsIndicator
        val viewPager = binding.vp
        pagerAdapter = PagerAdapter(
            list,
            list.size,
            childFragmentManager,
            FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        )
        viewPager.adapter = pagerAdapter
        tab.setViewPager(viewPager)

    }


}