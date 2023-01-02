package com.mrntlu.recyclerviewguide.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrntlu.recyclerviewguide.models.RecyclerViewModel
import com.mrntlu.recyclerviewguide.repository.MainRepository
import com.mrntlu.recyclerviewguide.utils.NetworkResponse
import com.mrntlu.recyclerviewguide.utils.RVState
import com.mrntlu.recyclerviewguide.utils.canPaginate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {
    private val repository = MainRepository()

    private val _rvList = MutableLiveData<RecyclerViewModel>()
    val rvList: LiveData<RVState<RecyclerViewModel>> = _rvList

    private var page: Int = 1

    init {
        fetchData()
    }

    fun resetPage() {
        page = 1
    }

    fun fetchData() = viewModelScope.launch {
        repository.fetchData(page).collect { state ->
            withContext(Dispatchers.Main) {
                when (state) {
                    is NetworkResponse.Failure -> {
                        if (page == 1) {
                            _rvList.value = RVState.Error(state.errorMessage)
                        } else {
                            _rvList.value = RVState.View(
                                (_rvList.value as RVState.View).list,
                                paginationErrorMessage = state.errorMessage,
                                isPaginationExhausted = true,
                            )
                        }
                    }
                    NetworkResponse.Loading -> {
                        if (page == 1) {
                            _rvList.value = RVState.Loading
                        } else {
                            _rvList.value = RVState.View(
                                (_rvList.value as RVState.View).list,
                                isPaginating = true,
                            )
                        }
                    }
                    is NetworkResponse.Success -> {
                        if (page == 1) {
                            _rvList.value = RVState.View(state.data)
                        } else {
                            val viewState = (_rvList.value as RVState.View<RecyclerViewModel>)
                            viewState.list.addAll(
                                state.data
                            )
                            viewState.isPaginating = false
                        }

                        page += 1
                    }
                }
            }
        }
    }
}