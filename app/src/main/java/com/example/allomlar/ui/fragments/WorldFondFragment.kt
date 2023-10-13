package com.mac.allomalar.ui.fragments

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.mac.allomalar.R
import com.mac.allomalar.adapters.WorldFondAdapter
import com.mac.allomalar.databinding.FragmentWorldFondBinding
import com.mac.allomalar.models.Book
import com.mac.allomalar.models.Science
import com.mac.allomalar.models.Status
import com.mac.allomalar.ui.activities.AllomalarActivity
import com.mac.allomalar.utils.NetworkHelper
import com.mac.allomalar.utils.NetworkStateChangeReceiver
import com.mac.allomalar.view_models.WorldFondViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val ARG_ALLOMA_ID = "alloma_id"
private const val ARG_ALLOMA_NAME = "alloma_name"

@AndroidEntryPoint
class WorldFondFragment : Fragment(), NetworkStateChangeReceiver.ConnectivityReceiverListener {

    @Inject
    lateinit var networkHelper: NetworkHelper
    private lateinit var binding: FragmentWorldFondBinding
    private var allomaId: Int = -1
    private var allomaName: String? = null
    private lateinit var adapter: WorldFondAdapter
    private val viewModel: WorldFondViewModel by viewModels()
    private val uiScope = CoroutineScope(Dispatchers.Main)
    private var isOnly = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            allomaId = it.getInt(ARG_ALLOMA_ID)
            allomaName = it.getString(ARG_ALLOMA_NAME)
        }
        activity?.registerReceiver(
            NetworkStateChangeReceiver(),
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
        NetworkStateChangeReceiver.connectivityReceiverListener = this
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWorldFondBinding.inflate(layoutInflater)
        binding.toolBar.title = allomaName

        uiScope.launch {
            try {
                val image =
                    viewModel.getImageById(viewModel.getAllomaById(allomaId).image_url)?.image
                if (image == null) {
                    binding.imageOfAlloma.setImageResource(R.drawable.old_me)
                } else {
                    binding.imageOfAlloma.setImageBitmap(image)
                }
            } catch (e: Exception) {
            }
        }

      if (!networkHelper.isNetworkConnected()) {
          uiScope.launch {
              readAllScienceFromRoom()
          }
      }
        return binding.root
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (isConnected && isOnly) {
            getAllSubjectFromApi()
            binding.progressBarWorldFond.visibility = View.VISIBLE
        }
    }

    private fun getAllSubjectFromApi() {
        uiScope.async {
            viewModel.allScience.observe(viewLifecycleOwner) { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        val job = CoroutineScope(Dispatchers.Main).launch {
                            viewModel.insertAllSciences(resource.data)
                        }

                        CoroutineScope(Dispatchers.Main).launch {
                            job.join()
                            readAllScienceFromRoom()
                            isOnly = false
                            binding.progressBarWorldFond.visibility = View.INVISIBLE
                        }
                    }
                }
            }
        }
    }

    private fun readAllScienceFromRoom() = uiScope.launch {
        val list = viewModel.getAllScienceFromRoom(allomaId)
        setAdapter(list)
    }

    private fun setAdapter(list: List<Science>) {
        adapter = WorldFondAdapter(list)
        binding.rvWorldFond.adapter = adapter
    }

}