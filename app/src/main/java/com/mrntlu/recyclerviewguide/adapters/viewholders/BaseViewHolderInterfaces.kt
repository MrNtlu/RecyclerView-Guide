package com.mrntlu.recyclerviewguide.adapters.viewholders

import com.mrntlu.recyclerviewguide.interfaces.Interaction

interface ItemViewHolderBind<T> {
    fun bind(item: T, position: Int, interaction: Interaction<T>)
}

interface ErrorViewHolderBind<T> {
    fun bind(errorMessage: String?, interaction: Interaction<T>)
}

interface PaginationExhaustViewHolderBind<T> {
    fun bind(interaction: Interaction<T>)
}