package com.mrntlu.recyclerviewguide.utils

sealed class RVState<out T>(
    open val value: Int
) {
    object Empty: RVState<Nothing>(RecyclerViewEnum.Empty.value)
    object Loading: RVState<Nothing>(RecyclerViewEnum.Loading.value)

    data class Error(
        val message: String,
    ): RVState<Nothing>(RecyclerViewEnum.Error.value)

    data class View<T>(
        override var list: List<T>,
        var isPaginating: Boolean = false,
        var isPaginationExhausted: Boolean = false,
        var paginationErrorMessage: String? = null,
    ): RVState<T>(RecyclerViewEnum.View.value), DataHolderState<T>

    data class CUDOperation<T>( //Insert Update Delete
        override var list: List<T>,
        val operation: CUDOperations,
    ): RVState<T>(RecyclerViewEnum.CUDOperation.value), DataHolderState<T>
}

enum class CUDOperations {
    Create,
    Update,
    Delete,
    Prepend,
}

interface DataHolderState<T> {
    var list: List<T>
}