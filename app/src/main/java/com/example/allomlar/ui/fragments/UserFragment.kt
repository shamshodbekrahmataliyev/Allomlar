package com.mac.allomalar.ui.fragments

import android.annotation.SuppressLint
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mac.allomalar.R
import com.mac.allomalar.adapters.UserAdapter
import com.mac.allomalar.databinding.FragmentUserBinding
import com.mac.allomalar.models.Alloma
import com.mac.allomalar.models.Image
import com.mac.allomalar.models.Status
import com.mac.allomalar.ui.activities.AllomalarActivity
import com.mac.allomalar.utils.NetworkHelper
import com.mac.allomalar.utils.NetworkStateChangeReceiver
import com.mac.allomalar.view_models.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_user.*
import kotlinx.coroutines.*
import javax.inject.Inject

private const val TAG = "UserFragment0"

@AndroidEntryPoint
class UserFragment : Fragment(), NetworkStateChangeReceiver.ConnectivityReceiverListener {

    @Inject
    lateinit var networkHelper: NetworkHelper
    private lateinit var binding: FragmentUserBinding
    private lateinit var adapter: UserAdapter
    private var list: ArrayList<Alloma> = ArrayList()
    private var listSearch: ArrayList<Alloma> = ArrayList()
    private val viewModel: UserViewModel by viewModels()
    private val uiScope = CoroutineScope(Dispatchers.Main)
    private val uiScopeIO = CoroutineScope(Dispatchers.IO)
    private var isFirstTime = true
    private var isReadFromRoom = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserBinding.inflate(layoutInflater)
        activity?.registerReceiver(
            NetworkStateChangeReceiver(),
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
        NetworkStateChangeReceiver.connectivityReceiverListener = this

        if (!networkHelper.isNetworkConnected()) {
            readAllomasFromRoom()
        } else if (networkHelper.isNetworkConnected() && AllomalarActivity.isdownloaded) {
            readAllomasFromRoom()
        }
        Log.d(TAG, "onCreateView: check")
        return binding.root
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (isConnected && AllomalarActivity.isFirstTimeToEnterUserFragment && !AllomalarActivity.isAllomasReadFromApi) {
            Toast.makeText(requireContext(), "Internetga ulanmoqda...", Toast.LENGTH_SHORT).show()
            if (!isReadFromRoom) {
                binding.progressBarPlayer.visibility = View.VISIBLE
                downloadAllAllomas()
            }
            AllomalarActivity.isFirstTimeToEnterUserFragment = false
        }
    }

    private fun readAllomasFromRoom() = uiScope.launch {
        list.clear()
        list.addAll(viewModel.getAllAllomasFromRoom())
        setAdapter(list)
        if (list.isNotEmpty())
            isReadFromRoom = true
        setClick()
    }

    override fun onResume() {
        super.onResume()
        binding.etSearchAllomaByName.setText("")
    }

    private fun setAdapter(list: ArrayList<Alloma>) {
        adapter = UserAdapter(requireContext(), list, object : UserAdapter.OnItemUserClick {
            override fun onClick(alloma: Alloma?, position: Int) {
                val bundle = Bundle()
                bundle.putInt("alloma_id", alloma?.id!!)
                findNavController().navigate(R.id.action_fr_user_to_scholar_1Fragment, bundle)
            }
        })

        binding.rvPlayer.adapter = adapter
    }


    private fun setClick() {
        binding.searchButton.setOnClickListener {
            search(binding.etSearchAllomaByName.text.toString())
        }

        binding.etSearchAllomaByName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isEmpty()) {
                    setAdapter(list)
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }


    private fun search(part: String) {
        listSearch.clear()
        list.forEach {
            if (it.name.toLowerCase().contains(part.toLowerCase())) {
                listSearch.add(it)
            }
            setAdapter(listSearch)
        }
    }

    private fun downloadAllAllomas() {
        uiScope.launch {
            viewModel.allAllomas.observe(viewLifecycleOwner) { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        val job = CoroutineScope(Dispatchers.Main).launch {
                            viewModel.insertAllomas(resource.data)
                        }
                        var job2 = CoroutineScope(Dispatchers.Main).launch {
                            job.join()
                            readAllomasFromRoom()
                            binding.progressBarPlayer.visibility = View.INVISIBLE
                            AllomalarActivity.isAllomasReadFromApi = true
                            AllomalarActivity.isFirstTimeToEnterUserFragment = false
                            AllomalarActivity.isdownloaded = true
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        uiScope.cancel()
        uiScopeIO.cancel()
    }
}