package com.mrntlu.recyclerviewguide.repository

import com.mrntlu.recyclerviewguide.models.RecyclerViewModel
import com.mrntlu.recyclerviewguide.utils.NetworkResponse
import com.mrntlu.recyclerviewguide.utils.Operation
import com.mrntlu.recyclerviewguide.utils.OperationEnum
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.util.*
import kotlin.collections.ArrayList

const val PAGE_SIZE = 10

class MainRepository {

    private val tempList = arrayListOf<RecyclerViewModel>().apply {
        for (i in 0..PAGE_SIZE) {
            add(RecyclerViewModel(UUID.randomUUID().toString(), "Content $i"),)
        }
    }

    fun fetchData(page: Int): Flow<NetworkResponse<ArrayList<RecyclerViewModel>>> = flow {
        emit(NetworkResponse.Loading(page != 1))

        kotlinx.coroutines.delay(2000L)

        try {
            if (page == 1)
                emit(NetworkResponse.Success(tempList.toList() as ArrayList<RecyclerViewModel>))
            else {
                val tempPaginationList = arrayListOf<RecyclerViewModel>().apply {
                    for (i in 0..PAGE_SIZE) {
                        add(RecyclerViewModel(UUID.randomUUID().toString(), "Content ${i * 2}"),)
                    }
                }

                if (page < 4) {
                    emit(NetworkResponse.Success(
                        tempPaginationList,
                        isPaginationData = true,
                    ))
                } else {
                    emit(NetworkResponse.Success(
                        arrayListOf(),
                        isPaginationData = true,
                        isPaginationExhausted = true
                    ))
                }
            }
        } catch (e: Exception) {
            if (page != 1) {
                emit(NetworkResponse.Success(
                    arrayListOf(),
                    isPaginationData = true,
                    isPaginationExhausted = true
                ))
            } else {
                emit(NetworkResponse.Failure(
                    e.message ?: e.toString(),
                ))
            }
        }
    }.flowOn(Dispatchers.IO)

    fun deleteData(item: RecyclerViewModel): Flow<NetworkResponse<Operation<RecyclerViewModel>>> = flow {
        kotlinx.coroutines.delay(1000L)

        try {
            emit(NetworkResponse.Success(Operation(item, OperationEnum.Delete)))
        } catch (e: Exception) {
            emit(NetworkResponse.Failure(e.message ?: e.toString()))
        }
    }.flowOn(Dispatchers.IO)

    fun updateData(item: RecyclerViewModel): Flow<NetworkResponse<Operation<RecyclerViewModel>>> = flow {
        kotlinx.coroutines.delay(1000L)

        try {
            item.content = "Updated Content ${(0..10).random()}"
            emit(NetworkResponse.Success(Operation(item, OperationEnum.Update)))
        } catch (e: Exception) {
            emit(NetworkResponse.Failure(e.message ?: e.toString()))
        }
    }.flowOn(Dispatchers.IO)

    fun toggleLikeData(item: RecyclerViewModel): Flow<NetworkResponse<Operation<RecyclerViewModel>>> = flow {
        kotlinx.coroutines.delay(1000L)

        try {
            item.isLiked = !item.isLiked
            emit(NetworkResponse.Success(Operation(item, OperationEnum.Update)))
        } catch (e: Exception) {
            emit(NetworkResponse.Failure(e.message ?: e.toString()))
        }
    }.flowOn(Dispatchers.IO)

    fun insertData(item: RecyclerViewModel): Flow<NetworkResponse<Operation<RecyclerViewModel>>> = flow {
        emit(NetworkResponse.Loading())

        kotlinx.coroutines.delay(1000L)

        try {
            emit(NetworkResponse.Success(Operation(item, operationEnum = OperationEnum.Insert)))
        } catch (e: Exception) {
            emit(NetworkResponse.Failure(e.message ?: e.toString()))
        }
    }.flowOn(Dispatchers.IO)
}