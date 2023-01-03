package com.mrntlu.recyclerviewguide.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mrntlu.recyclerviewguide.adapters.RecyclerViewAdapter
import com.mrntlu.recyclerviewguide.databinding.FragmentMainBinding
import com.mrntlu.recyclerviewguide.interfaces.Interaction
import com.mrntlu.recyclerviewguide.models.RecyclerViewModel
import com.mrntlu.recyclerviewguide.ui.BaseFragment
import com.mrntlu.recyclerviewguide.utils.NetworkResponse
import com.mrntlu.recyclerviewguide.utils.quickScrollToTop
import com.mrntlu.recyclerviewguide.viewmodels.MainViewModel
import kotlinx.coroutines.launch
import java.util.UUID

class MainFragment : BaseFragment<FragmentMainBinding>() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
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
        setObservers()
    }

    private fun setObservers() {
        viewModel.rvList.observe(viewLifecycleOwner) { response ->
            binding.swipeRefreshLayout.isEnabled = if (response is NetworkResponse.Success) {
                true
            } else if (response is NetworkResponse.Failure) {
                response.isPaginationError
            } else
                false

            when(response) {
                is NetworkResponse.Failure -> {
                    recyclerViewAdapter?.setError(response.errorMessage, response.isPaginationError)
                }
                is NetworkResponse.Loading -> {
                    recyclerViewAdapter?.setLoading(response.isPaginating)
                }
                is NetworkResponse.Success -> {
                    recyclerViewAdapter?.setData(response.data, response.isPaginationData)
                }
            }
        }

        viewModel.rvOperation.observe(viewLifecycleOwner) { response ->
            when(response) {
                is NetworkResponse.Failure -> {} //Show dialog
                is NetworkResponse.Loading -> {} //Show dialog
                is NetworkResponse.Success -> {
                    recyclerViewAdapter?.handleOperation(response.data)
                }
            }
        }
    }

    private fun setListeners() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshData()

            binding.swipeRefreshLayout.isRefreshing = false
        }

        binding.errorButton.setOnClickListener {
            viewModel.throwError()
        }

        binding.appendButton.setOnClickListener {
            if (recyclerViewAdapter?.canPaginate == true && recyclerViewAdapter?.isPaginating == false)
                viewModel.fetchData()
        }

        binding.insertButton.setOnClickListener {
            viewModel.insertData(RecyclerViewModel(UUID.randomUUID().toString()))
        }

        binding.paginateErrorButton.setOnClickListener {
            viewModel.exhaustPagination()
        }
    }

    private fun setRecyclerView() {
        binding.mainRV.apply {
            val linearLayoutManager = LinearLayoutManager(context)
            layoutManager = linearLayoutManager
            addItemDecoration(DividerItemDecoration(context, linearLayoutManager.orientation))
            recyclerViewAdapter = RecyclerViewAdapter(object: Interaction<RecyclerViewModel> {
                override fun onItemSelected(item: RecyclerViewModel) {
                    viewModel.deleteData(item)
                }

                override fun onLongPressed(item: RecyclerViewModel) {
                    viewModel.updateData(item.copy())
                }

                override fun onErrorRefreshPressed() {
                    viewModel.refreshData()
                }

                override fun onExhaustButtonPressed() {
                    viewLifecycleOwner.lifecycleScope.launch {
                        quickScrollToTop()
                    }
                }
            })
            adapter = recyclerViewAdapter

            var isScrolling = false
            addOnScrollListener(object: RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    isScrolling = newState != AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val itemCount = linearLayoutManager.itemCount
                    val lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition()

                    recyclerViewAdapter?.let {
                        if (
                            isScrolling &&
                            lastVisibleItemPosition >= itemCount.minus(5) &&
                            it.canPaginate &&
                            !it.isPaginating
                        ) {
                            viewModel.fetchData()
                        }
                    }
                }
            })
        }
    }
}