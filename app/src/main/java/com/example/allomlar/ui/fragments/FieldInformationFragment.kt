package com.mac.allomalar.ui.fragments

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.mac.allomalar.R
import com.mac.allomalar.adapters.FieldsInformationAdapter
import com.mac.allomalar.databinding.FragmentFieldInformationBinding
import com.mac.allomalar.models.Status
import com.mac.allomalar.models.SubjectInfo
import com.mac.allomalar.ui.activities.AllomalarActivity
import com.mac.allomalar.utils.NetworkHelper
import com.mac.allomalar.utils.NetworkStateChangeReceiver
import com.mac.allomalar.view_models.FieldInformationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

private const val ARG_PARAM1 = "alloma_id"
private const val ARG_PARAM2 = "field_id"
private const val ARG_PARAM3 = "field_name"

@AndroidEntryPoint
class FieldInformationFragment : Fragment(),
    NetworkStateChangeReceiver.ConnectivityReceiverListener {

    @Inject
    lateinit var networkHelper: NetworkHelper
    private var allomaId: Int? = null
    private var fieldId: Int? = null
    private var fieldName: String? = null
    private lateinit var binding: FragmentFieldInformationBinding
    private lateinit var adapter: FieldsInformationAdapter
    private val uiScope = CoroutineScope(Dispatchers.Main)
    private val viewModel: FieldInformationViewModel by viewModels()
    private var list: ArrayList<SubjectInfo> = ArrayList()
    private var isOnce = true
    private var isOnce1 = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            allomaId = it.getInt(ARG_PARAM1)
            fieldId = it.getInt(ARG_PARAM2)
            fieldName = it.getString(ARG_PARAM3)
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
        binding = FragmentFieldInformationBinding.inflate(layoutInflater)

        if (!networkHelper.isNetworkConnected()) {
            readSubjectsInfoMain()
        }
        binding.tvFieldName.text = fieldName
        return binding.root
    }


    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (isConnected && isOnce) {
            if (isOnce1) {
                try {
                    viewModel.getAllSubjectsInside(fieldId!!)
                    isOnce1 = false
                } catch (e: Exception) {
                }
            }
            readAllSubjectInfo()
            binding.progressScholar2.visibility = View.VISIBLE
        }
    }

    private fun readAllSubjectInfo() {
        uiScope.launch {
            var list1 = ArrayList<SubjectInfo>()
            viewModel.allSubjectsInfo.observe(viewLifecycleOwner) { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        try {
                            val job = CoroutineScope(Dispatchers.Main).launch {
                                list1 = resource.data as ArrayList<SubjectInfo>
                                viewModel.insertSubjectInfo(resource.data)
                            }

                            CoroutineScope(Dispatchers.Main).launch {
                                job.join()
                                binding.progressScholar2.visibility = View.INVISIBLE
                                if (list1.isEmpty()) {
                                    Toast.makeText(
                                        requireContext(),
                                        "Malumot yo'q",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    binding.progressScholar2.visibility = View.INVISIBLE
                                }
                                setAdapter(list1)
                                isOnce = false
                            }
                        } catch (e: Exception) {
                            Toast.makeText(requireContext(), "Here", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun readSubjectsInfoMain() = uiScope.launch {
        list.clear()
        list = viewModel.getAllSubjectsInfo(fieldId!!) as ArrayList<SubjectInfo>
        if (list.isEmpty()) {
            Toast.makeText(requireContext(), "Malumot yo'q", Toast.LENGTH_SHORT).show()
            binding.progressScholar2.visibility = View.INVISIBLE
        }
        setAdapter(list)
    }

    private fun setAdapter(list: List<SubjectInfo>) {
        adapter = FieldsInformationAdapter(list)
        binding.rv.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        list.clear()
    }
}