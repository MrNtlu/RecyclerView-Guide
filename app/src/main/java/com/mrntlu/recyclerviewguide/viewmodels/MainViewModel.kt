package com.mrntlu.recyclerviewguide.viewmodels

import androidx.lifecycle.*
import com.mrntlu.recyclerviewguide.models.RecyclerViewModel
import com.mrntlu.recyclerviewguide.repository.MainRepository
import com.mrntlu.recyclerviewguide.utils.NetworkResponse
import com.mrntlu.recyclerviewguide.utils.Operation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val PAGE_KEY = "rv.page"
const val SCROLL_POSITION_KEY = "rv.scroll_position"

class MainViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val repository = MainRepository()

    private val _rvList = MutableLiveData<NetworkResponse<ArrayList<RecyclerViewModel>>>()
    val rvList: LiveData<NetworkResponse<ArrayList<RecyclerViewModel>>> = _rvList

    private val _rvOperation = MutableLiveData<NetworkResponse<Operation<RecyclerViewModel>>>()
    val rvOperation: LiveData<NetworkResponse<Operation<RecyclerViewModel>>> = _rvOperation

    /** Process Death restore
     * isRestoringData is necessary to handle recyclerview.scrollToPosition
     * scrollPosition is middle item's position on recyclerview
     */
    var isRestoringData = false
    private var page: Int = savedStateHandle[PAGE_KEY] ?: 1
    var scrollPosition: Int = savedStateHandle[SCROLL_POSITION_KEY] ?: 0
        private set

    // Variable for detecting orientation change
    var didOrientationChange = false

    init {
        if (page != 1) {
            restoreData()
        } else {
            fetchData()
        }
    }

    fun refreshData() {
        setPagePosition(1)
        fetchData()
    }

    private fun restoreData() {
        isRestoringData = true

        var isPaginationExhausted = false
        val tempList = arrayListOf<RecyclerViewModel>()
        viewModelScope.launch(Dispatchers.IO) {
            for (p in 1..page) {
                val job = launch(Dispatchers.IO) {
                    repository.fetchData(p).collect { state ->
                        if (state is NetworkResponse.Success) {
                            tempList.addAll(state.data)
                            isPaginationExhausted = state.isPaginationExhausted
                        }
                    }
                }
                job.join()
            }
            withContext(Dispatchers.Main) {
                _rvList.value = NetworkResponse.Success(tempList, isPaginationData = true, isPaginationExhausted = isPaginationExhausted)
            }
        }
    }

    fun fetchData() = viewModelScope.launch(Dispatchers.IO) {
        repository.fetchData(page).collect { state ->
            withContext(Dispatchers.Main) {
                _rvList.value = state

                if (state is NetworkResponse.Success) {
                    setPagePosition(page.plus(1))
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
        _rvList.value = NetworkResponse.Success(
            arrayListOf(),
            isPaginationData = true,
            isPaginationExhausted = true,
        )
    }

    private fun setPagePosition(newPage: Int) {
        page = newPage
        savedStateHandle[PAGE_KEY] = newPage
    }

    fun setScrollPosition(newPosition: Int) {
        if (!isRestoringData && !didOrientationChange) {
            scrollPosition = newPosition
            savedStateHandle[SCROLL_POSITION_KEY] = newPosition
        }
    }
}