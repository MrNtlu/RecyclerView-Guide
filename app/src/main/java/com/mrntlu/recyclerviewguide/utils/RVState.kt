package com.mrntlu.recyclerviewguide.utils

sealed class RVState<out T>(
    open val rvEnum: RecyclerViewEnum
) {
    object Empty: RVState<Nothing>(RecyclerViewEnum.Empty)
    object Loading: RVState<Nothing>(RecyclerViewEnum.Loading)

    data class Error(
        val message: String,
    ): RVState<Nothing>(RecyclerViewEnum.Error)

    data class View<T>(
        var list: List<T>,
        override val rvEnum: RecyclerViewEnum,
    ): RVState<T>(rvEnum)
}