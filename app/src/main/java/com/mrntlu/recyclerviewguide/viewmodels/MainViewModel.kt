package com.mrntlu.recyclerviewguide.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrntlu.recyclerviewguide.models.RecyclerViewModel
import com.mrntlu.recyclerviewguide.repository.MainRepository
import com.mrntlu.recyclerviewguide.utils.NetworkResponse
import com.mrntlu.recyclerviewguide.utils.Operation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {
    private val repository = MainRepository()

    private val _rvList = MutableLiveData<NetworkResponse<ArrayList<RecyclerViewModel>>>()
    val rvList: LiveData<NetworkResponse<ArrayList<RecyclerViewModel>>> = _rvList

    private val _rvOperation = MutableLiveData<NetworkResponse<Operation<RecyclerViewModel>>>()
    val rvOperation: LiveData<NetworkResponse<Operation<RecyclerViewModel>>> = _rvOperation

    private var page: Int = 1

    init {
        fetchData()
    }

    fun refreshData() {
        page = 1
        fetchData()
    }

    fun fetchData() = viewModelScope.launch(Dispatchers.IO) {
        repository.fetchData(page).collect { state ->
            withContext(Dispatchers.Main) {
                _rvList.value = state

                if (state is NetworkResponse.Success) {
                    page += 1
                }
            }
        }
    }

    fun deleteData(item: RecyclerViewModel) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteData(item).collect { state ->
            withContext(Dispatchers.Main) {
                _rvOperation.value = state
            }
        }
    }

    fun updateData(item: RecyclerViewModel) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateData(item).collect { state ->
            withContext(Dispatchers.Main) {
                _rvOperation.value = state
            }
        }
    }

    fun toggleLikeData(item: RecyclerViewModel) = viewModelScope.launch(Dispatchers.IO) {
        repository.toggleLikeData(item).collect { state ->
            withContext(Dispatchers.Main) {
                _rvOperation.value = state
            }
        }
    }

    fun insertData(item: RecyclerViewModel) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertData(item).collect { state ->
            withContext(Dispatchers.Main) {
                _rvOperation.value = state
            }
        }
    }

    fun throwError() = viewModelScope.launch(Dispatchers.Main) {
        _rvList.value = NetworkResponse.Failure("Error occured!")
    }

    fun exhaustPagination() = viewModelScope.launch(Dispatchers.Main) {
        _rvList.value = NetworkResponse.Failure(
            "Pagination Exhaust",
            true
        )
    }
}