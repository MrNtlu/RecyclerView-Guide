package com.mrntlu.recyclerviewguide.models

data class RecyclerViewModel(
    var id: String,
) {
    val text: String
        get() = "ID: $id"
}
