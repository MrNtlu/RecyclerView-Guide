package com.mrntlu.recyclerviewguide.interfaces

interface Interaction<T> {
    fun onItemSelected(position: Int, item: T)
    fun onLongPressed(position: Int, item: T)

    fun onErrorRefreshPressed()
}