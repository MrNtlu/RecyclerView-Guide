package com.mrntlu.recyclerviewguide.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mrntlu.recyclerviewguide.R
import com.mrntlu.recyclerviewguide.adapters.RecyclerViewAdapter
import com.mrntlu.recyclerviewguide.databinding.FragmentMainBinding
import com.mrntlu.recyclerviewguide.interfaces.Interaction
import com.mrntlu.recyclerviewguide.models.RecyclerViewModel
import com.mrntlu.recyclerviewguide.ui.BaseFragment
import com.mrntlu.recyclerviewguide.utils.CUDOperations
import com.mrntlu.recyclerviewguide.utils.RVState
import com.mrntlu.recyclerviewguide.utils.RecyclerViewEnum
import com.mrntlu.recyclerviewguide.utils.printLog
import kotlinx.coroutines.*
import java.util.UUID

class MainFragment : BaseFragment<FragmentMainBinding>() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private val tempList = mutableListOf<RecyclerViewModel>(
        RecyclerViewModel(UUID.randomUUID().toString()),
        RecyclerViewModel(UUID.randomUUID().toString()),
        RecyclerViewModel(UUID.randomUUID().toString()),
        RecyclerViewModel(UUID.randomUUID().toString()),
        RecyclerViewModel(UUID.randomUUID().toString()),
        RecyclerViewModel(UUID.randomUUID().toString()),
        RecyclerViewModel(UUID.randomUUID().toString()),
        RecyclerViewModel(UUID.randomUUID().toString()),
        RecyclerViewModel(UUID.randomUUID().toString()),
        RecyclerViewModel(UUID.randomUUID().toString()),
        RecyclerViewModel(UUID.randomUUID().toString()),
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
            recyclerViewAdapter?.setData(RVState.Error("Custom error message"))
        }

        binding.appendButton.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                recyclerViewAdapter?.setData(
                    RVState.View(
                        tempList,
                        isPaginating = true,
                    )
                )

                delay(3000L)

                tempList.add(tempList.size, RecyclerViewModel(UUID.randomUUID().toString()))
                tempList.add(tempList.size, RecyclerViewModel(UUID.randomUUID().toString()))
                tempList.add(tempList.size, RecyclerViewModel(UUID.randomUUID().toString()))
                tempList.add(tempList.size, RecyclerViewModel(UUID.randomUUID().toString()))
                tempList.add(tempList.size, RecyclerViewModel(UUID.randomUUID().toString()))

                recyclerViewAdapter?.setData(RVState.View(
                    tempList.toList(),
                    isPaginating = false
                ))

                binding.mainRV.scrollToPosition(tempList.size - 1)
            }
        }

        binding.prependButton.setOnClickListener {
            tempList.add(0, RecyclerViewModel(UUID.randomUUID().toString()))

            recyclerViewAdapter?.setData(RVState.CUDOperation(
                tempList,
                CUDOperations.Prepend,
            ))

            binding.mainRV.scrollToPosition(0)
        }

        binding.loadingButton.setOnClickListener {
            recyclerViewAdapter?.setData(RVState.Loading)
        }

        binding.insertButton.setOnClickListener {
            tempList.add(tempList.size, RecyclerViewModel(UUID.randomUUID().toString()))

            recyclerViewAdapter?.setData(RVState.CUDOperation(
                tempList,
                CUDOperations.Create,
            ))

            binding.mainRV.scrollToPosition(tempList.size - 1)
        }

        binding.paginateErrorButton.setOnClickListener {
            recyclerViewAdapter?.setData(RVState.View(
                tempList,
                paginationErrorMessage = "Pagination example error"
            ))
        }

        binding.clearButton.setOnClickListener {
            tempList.clear()
            recyclerViewAdapter?.setData(RVState.Empty)
        }

        binding.viewButton.setOnClickListener {
            recyclerViewAdapter?.setData(RVState.View(
                tempList,
            ))
        }
    }

    private fun setRecyclerView() {
        binding.mainRV.apply {
            val linearLayoutManager = LinearLayoutManager(context)
            layoutManager = linearLayoutManager
            addItemDecoration(DividerItemDecoration(context, linearLayoutManager.orientation))
            recyclerViewAdapter = RecyclerViewAdapter(object: Interaction<RecyclerViewModel> {
                override fun onItemSelected(position: Int, item: RecyclerViewModel) {
                    tempList.removeAt(position)

                    recyclerViewAdapter?.setData(RVState.CUDOperation(
                        tempList,
                        CUDOperations.Delete
                    ))
                }

                override fun onLongPressed(position: Int, item: RecyclerViewModel) {
                    item.id = "Test ID"
                    tempList[position] = item

                    recyclerViewAdapter?.setData(RVState.View(
                        tempList,
                    ))
                }

                override fun onErrorRefreshPressed() {
                    TODO("Not yet implemented")
                }
            })
            adapter = recyclerViewAdapter
            recyclerViewAdapter?.setData(RVState.View(tempList.toList()))
        }
    }
}