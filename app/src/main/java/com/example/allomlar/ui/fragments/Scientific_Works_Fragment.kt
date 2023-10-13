package com.mac.allomalar.ui.fragments

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.mac.allomalar.R
import com.mac.allomalar.adapters.ScientificWorksAdapter
import com.mac.allomalar.databinding.FragmentScientificWorksBinding
import com.mac.allomalar.models.Book
import com.mac.allomalar.models.Status
import com.mac.allomalar.ui.activities.AllomalarActivity
import com.mac.allomalar.utils.NetworkHelper
import com.mac.allomalar.utils.NetworkStateChangeReceiver
import com.mac.allomalar.view_models.ScientificWorksViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val ARG_PARAM = "alloma_id"

@AndroidEntryPoint
class ScientificWorksFragment : Fragment(),
    NetworkStateChangeReceiver.ConnectivityReceiverListener {

    @Inject
    lateinit var networkHelper: NetworkHelper

    private var allomaId: Int = -1
    private lateinit var binding: FragmentScientificWorksBinding
    private lateinit var adapter: ScientificWorksAdapter
    private val viewModel: ScientificWorksViewModel by viewModels()
    private val list: ArrayList<Book> = ArrayList()
    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)
    private var isOnly = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            allomaId = it.getInt(ARG_PARAM)
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
        binding = FragmentScientificWorksBinding.inflate(layoutInflater)

        if (!networkHelper.isNetworkConnected()){
            uiScope.launch {
                getBooksFromRoom()
            }
        }
        AllomalarActivity.isFirstTimeToEnterBooksFragment = false
        return binding.root
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (isConnected && isOnly) {
            /**
             * && AllomalarActivity.isFirstTimeToEnterBooksFragment && !AllomalarActivity.isBooksAreWrittenToRoom
            * */
            readFromApi()
            binding.progressScholar2.visibility = View.VISIBLE
        }
    }

    private fun readFromApi() {
        uiScope.launch {
            viewModel.allBooks.observe(viewLifecycleOwner) { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        val job = CoroutineScope(Dispatchers.Main).launch {
                            viewModel.insertAllBooks(resource.data)
                        }

                        CoroutineScope(Dispatchers.Main).launch {
                            job.join()
                            getBooksFromRoom()
                            isOnly = false
                            binding.progressScholar2.visibility = View.INVISIBLE
                        }
                    }
                }
            }
        }
    }

    private fun getBooksFromRoom() = uiScope.launch {
        list.addAll(viewModel.getAllBooksFromRoom(allomaId))
        setAdapter()
    }

    private fun setAdapter() {
        adapter = ScientificWorksAdapter(list)
        binding.rv.adapter = adapter
    }
}