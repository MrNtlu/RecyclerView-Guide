package com.mrntlu.recyclerviewguide.repository

import com.mrntlu.recyclerviewguide.models.RecyclerViewModel
import com.mrntlu.recyclerviewguide.utils.NetworkResponse
import com.mrntlu.recyclerviewguide.utils.RVState
import com.mrntlu.recyclerviewguide.utils.RecyclerViewEnum
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.util.*

class MainRepository {

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

    private val tempPaginationList = mutableListOf<RecyclerViewModel>(
        RecyclerViewModel(UUID.randomUUID().toString()),
        RecyclerViewModel(UUID.randomUUID().toString()),
        RecyclerViewModel(UUID.randomUUID().toString()),
        RecyclerViewModel(UUID.randomUUID().toString()),
        RecyclerViewModel(UUID.randomUUID().toString()),
    )

    fun fetchData(page: Int): Flow<NetworkResponse<MutableList<RecyclerViewModel>>> = flow {
        emit(NetworkResponse.Loading)

        kotlinx.coroutines.delay(1000L)

        try {
            if (page == 1)
                emit(NetworkResponse.Success(tempList))
            else
                emit(NetworkResponse.Success(tempPaginationList))
        } catch (e: Exception) {
            emit(NetworkResponse.Failure(e.message ?: e.toString()))
        }
    }.flowOn(Dispatchers.IO)

    fun deleteData(item: RecyclerViewModel) = flow {
        kotlinx.coroutines.delay(1000L)

        try {

        } catch (e: Exception) {
            emit(RVState.Error(e.message ?: e.toString()))
        }
    }.flowOn(Dispatchers.IO)
}