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
import androidx.navigation.fragment.findNavController
import com.mac.allomalar.R
import com.mac.allomalar.adapters.FieldsAdapter
import com.mac.allomalar.databinding.FragmentScholars2Binding
import com.mac.allomalar.models.Status
import com.mac.allomalar.models.Subject
import com.mac.allomalar.ui.activities.AllomalarActivity
import com.mac.allomalar.utils.NetworkHelper
import com.mac.allomalar.utils.NetworkStateChangeReceiver
import com.mac.allomalar.view_models.Scholar2ViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

private const val ARG_ALLOMA_ID = "alloma_id"

@AndroidEntryPoint
class Scholars_2Fragment : Fragment(), NetworkStateChangeReceiver.ConnectivityReceiverListener {

    @Inject
    lateinit var networkHelper: NetworkHelper

    private var allomaId = -1
    private lateinit var fieldsAdapter: FieldsAdapter
    private lateinit var binding: FragmentScholars2Binding
    private val viewModel: Scholar2ViewModel by viewModels()
    private val uiScope = CoroutineScope(Dispatchers.Main)
    private lateinit var list: List<Subject?>
    private var isOnly = true
    private var firstResume = false
    private var finishReadingFromApi = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.registerReceiver(
            NetworkStateChangeReceiver(),
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
        NetworkStateChangeReceiver.connectivityReceiverListener = this

        arguments?.let {
            allomaId = it.getInt(ARG_ALLOMA_ID)
        }
        list = ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentScholars2Binding.inflate(layoutInflater)

        if (!networkHelper.isNetworkConnected()) {
            readSubjects()
            readAlloma(allomaId)
            AllomalarActivity.isFirstTimeToEnterUserFragment = false
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        if (networkHelper.isNetworkConnected() && firstResume) {
            readSubjects()
            readAlloma(allomaId)
        }
        firstResume = true
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (isConnected && !finishReadingFromApi) {
            try {
                try {
                    viewModel.getAllSubjectsInside(allomaId)
                    readAllSubject()
                } catch (e: Exception) {
                }
                binding.progressScholar2.visibility = View.VISIBLE
            } catch (e: Exception) {
            }

        } else if (isConnected && finishReadingFromApi) {
            readSubjects()
            readAlloma(allomaId)
        }
    }

    private fun readAllSubject() {
        uiScope.launch {
            try {
                viewModel.allSubjects.observe(viewLifecycleOwner) { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            val job = CoroutineScope(Dispatchers.Main).launch {
                                viewModel.insertSubjects(resource.data)
                            }

                            CoroutineScope(Dispatchers.Main).launch {
                                binding.progressScholar2.visibility = View.INVISIBLE
                                job.join()
                                readSubjects()
                                finishReadingFromApi = true
                                readAlloma(allomaId)

                            }
                        }
                    }
                }
            } catch (e: Exception) {
            }
        }
    }


    private fun readAlloma(allomaID: Int) {
        uiScope.launch {
            var alloma = viewModel.getAllomaById(allomaID)
            try {
                (alloma.name + "\n" + alloma.birth_year).also { binding.tvScholarName.text = it }
                val image = viewModel.getImageById(alloma.image_url)
                if (image == null) {
                    binding.ivScholar.setImageResource(R.drawable.old_me)
                } else {
                    binding.ivScholar.setImageBitmap(image?.image)
                }

            } catch (e: Exception) {
                Log.d("TAG", "readAlloma: xatolik rasm bilan")
            }
        }
    }

    private fun readSubjects() = uiScope.launch {
        list = viewModel.getAllSubjects(allomaId)
        setAdapter(list)
    }

    private fun setAdapter(list: List<Subject?>) {
        fieldsAdapter = FieldsAdapter(requireContext(), list, object : FieldsAdapter.OnFieldClick {
            override fun onClick(subject: Subject?, position: Int) {
                val bundle = Bundle()
                bundle.putInt("alloma_id", allomaId)
                bundle.putInt("field_id", subject?.id!!)
                bundle.putString("field_name", subject.name)

                findNavController().navigate(
                    R.id.action_scholars_2Fragment_to_fieldInformationFragment,
                    bundle
                )
            }
        })
        binding.rv.adapter = fieldsAdapter
    }
}