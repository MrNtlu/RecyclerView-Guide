package com.mrntlu.recyclerviewguide.interfaces

interface Interaction<T> {
    fun onItemSelected(item: T)

    fun onErrorRefreshPressed()
    fun onExhaustButtonPressed()
}