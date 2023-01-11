package com.mrntlu.recyclerviewguide.interfaces

interface Interaction<T> {
    fun onItemSelected(item: T, position: Int)

    fun onErrorRefreshPressed()
    fun onExhaustButtonPressed()
}