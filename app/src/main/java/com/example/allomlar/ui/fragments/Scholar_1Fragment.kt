package com.mac.allomalar.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.mac.allomalar.R
import com.mac.allomalar.databinding.FragmentScholar1Binding
import com.mac.allomalar.models.Alloma
import com.mac.allomalar.view_models.Scholar1ViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

private const val ARG_ALLOMA_ID = "alloma_id"

@AndroidEntryPoint
class Scholar_1Fragment : Fragment() {
    private var allomaID = -1
    private lateinit var binding: FragmentScholar1Binding
    private lateinit var viewModel: Scholar1ViewModel
    var job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)
    private var alloma: Alloma? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            allomaID = it.getInt(ARG_ALLOMA_ID)
        }
        viewModel = ViewModelProvider(this).get(Scholar1ViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentScholar1Binding.inflate(layoutInflater)
        setListeners()
        setBindings()
        return binding.root
    }

    private fun setBindings() {
        val job = uiScope.launch {
            alloma = viewModel.getAlloma(allomaID)
        }

        uiScope.launch {
            job.join()
            binding.tvName.text = alloma?.name
            binding.tvLifeYears.text = alloma?.birth_year
            try {
                var image = viewModel.repository.getImageFromRoomById(
                    alloma?.image_url!!
                )?.image

                if (image == null) {
                    binding.ivScholarImage.setImageResource(R.drawable.old_me)
                } else {
                    binding.ivScholarImage.setImageBitmap(image)
                }
            } catch (e: Exception) {

            }
        }
    }

    private fun setListeners() {
        binding.tvIjodYunalishlari.setOnClickListener {
            navigate1()
        }

        binding.image1.setOnClickListener {
            navigate1()
        }

        binding.tvScientificWorks.setOnClickListener {
            navigate2()
        }

        binding.image2.setOnClickListener {
            navigate2()
        }

        binding.tvWorldFond.setOnClickListener {
            navigate3()
        }

        binding.image3.setOnClickListener {
            navigate3()
        }

    }

    private fun navigate1() {
        val bundle = Bundle()
        bundle.putInt("alloma_id", allomaID)
        findNavController().navigate(R.id.action_scholar_1Fragment_to_scholars_2Fragment, bundle)
    }

    private fun navigate2() {
        val bundle = Bundle()
        bundle.putInt("alloma_id", allomaID)
        findNavController().navigate(
            R.id.action_scholar_1Fragment_to_scientificWorksFragment,
            bundle
        )
    }

    private fun navigate3() {
        val bundle = Bundle()
        bundle.putInt("alloma_id", allomaID)
        bundle.putString("alloma_name", alloma?.name)
        findNavController().navigate(R.id.action_scholar_1Fragment_to_worldFondFragment, bundle)
    }


}