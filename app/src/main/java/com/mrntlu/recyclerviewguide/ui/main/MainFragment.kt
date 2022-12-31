package com.mrntlu.recyclerviewguide.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mrntlu.recyclerviewguide.R
import com.mrntlu.recyclerviewguide.adapters.RecyclerViewAdapter
import com.mrntlu.recyclerviewguide.databinding.FragmentMainBinding
import com.mrntlu.recyclerviewguide.interfaces.Interaction
import com.mrntlu.recyclerviewguide.models.RecyclerViewModel
import com.mrntlu.recyclerviewguide.ui.BaseFragment

class MainFragment : BaseFragment<FragmentMainBinding>() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private val tempList = listOf(
        RecyclerViewModel(1, "Text 1", 0),
        RecyclerViewModel(2, "Text 2", 1),
        RecyclerViewModel(3, "Text 3", 2),
        RecyclerViewModel(4, "Text 4", 3),
        RecyclerViewModel(5, "Text 5", 4),
        RecyclerViewModel(6, "Text 6", 5),
        RecyclerViewModel(7, "Text 7", 6),
        RecyclerViewModel(8, "Text 8", 7),
        RecyclerViewModel(9, "Text 9", 8),
        RecyclerViewModel(10, "Text 10", 9),
        RecyclerViewModel(11, "Text 11", 10),
    )
    private var recyclerViewAdapter: RecyclerViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
        setRecyclerView()
    }

    private fun setListeners() {
        binding.errorButton.setOnClickListener {

        }

        binding.appendButton.setOnClickListener {

        }

        binding.prependButton.setOnClickListener {

        }

        binding.exhaustButton.setOnClickListener {

        }
    }

    private fun setRecyclerView() {
        binding.mainRV.apply {
            val linearLayoutManager = LinearLayoutManager(context)
            layoutManager = linearLayoutManager
            addItemDecoration(DividerItemDecoration(context, linearLayoutManager.orientation))
            recyclerViewAdapter = RecyclerViewAdapter(object: Interaction<RecyclerViewModel> {
                override fun onItemSelected(position: Int, item: RecyclerViewModel) {
                    TODO("Not yet implemented")
                }

                override fun onErrorRefreshPressed() {
                    TODO("Not yet implemented")
                }
            })
            adapter = recyclerViewAdapter
        }
    }
}