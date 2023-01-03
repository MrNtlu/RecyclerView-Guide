package com.mrntlu.recyclerviewguide.interfaces

interface Interaction<T> {
    fun onItemSelected(item: T)
    fun onLongPressed(item: T)

    fun onErrorRefreshPressed()
    fun onExhaustButtonPressed()
}