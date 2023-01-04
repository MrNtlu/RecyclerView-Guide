package com.mrntlu.recyclerviewguide.models

import java.util.*

data class RecyclerViewModel(
    var id: String,
    var content: String = "",
    var isLiked: Boolean = false,
) {
    val text: String
        get() = "ID: $id"

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        if (other !is RecyclerViewModel)
            return false
        return other.id == id
    }

    override fun hashCode() = Objects.hash(id)
}
