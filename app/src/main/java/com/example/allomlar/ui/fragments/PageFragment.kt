package com.mac.allomalar.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mac.allomalar.R
import com.mac.allomalar.adapters.MadrasasAdapter
import com.mac.allomalar.databinding.FragmentPagerBinding
import com.mac.allomalar.models.Century
import com.mac.allomalar.models.Madrasa
import com.mac.allomalar.view_models.PagerFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class PagerFragment(century: Century) : Fragment() {
    private lateinit var binding: FragmentPagerBinding
    private val century = century
    private val viewModel: PagerFragmentViewModel by viewModels()
    private var list = ArrayList<Madrasa>()
    private var listPre = ArrayList<Madrasa>()
    var job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    private lateinit var adapter: MadrasasAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPagerBinding.inflate(inflater)
        setBindings()
        uiScope.launch {
            setData()
        }
        return binding.root
    }

    private fun setData() {
        CoroutineScope(Dispatchers.Main).launch {
            val list1 = viewModel.getAllMadrasaFromRoom()
            list.clear()
            list1.forEach {
                if (century.id == it.century_id)
                    list.add(it)
            }
            setAdapter(list, listPre)
        }
    }


    private fun setBindings() {
        binding.tvCentury.text = century.century
        binding.tvNumberOfScholars.text = century.sum_madrasa
    }

    private fun setAdapter(list: List<Madrasa?>?, listPre: ArrayList<Madrasa>) {
        adapter =
            MadrasasAdapter(list!!, listPre, century, object : MadrasasAdapter.MadrasaSetOnClickListener {
                override fun onMadrasaClickListener(madrasa: Madrasa, position: Int) {
                   val bundle = Bundle()
                    bundle.putString("madrasa_name", madrasa.name)
                    bundle.putInt("madrasa_id", madrasa.id)
                    findNavController().navigate(R.id.action_fr_home_to_madrasaFragment, bundle)
                }
            })

        binding.rv.adapter = adapter
    }


    companion object {
        fun getInstance(century: Century): PagerFragment = PagerFragment(century)
    }

}