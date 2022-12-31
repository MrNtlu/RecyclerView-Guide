package com.mrntlu.recyclerviewguide.utils

enum class RecyclerViewState(val value: Int) {
    Loading(0),
    View(1),
    Error(2),
    NoItem(3),
    PaginationLoading(4),
    PaginationError(5),
    PaginationExhaust(6)
}