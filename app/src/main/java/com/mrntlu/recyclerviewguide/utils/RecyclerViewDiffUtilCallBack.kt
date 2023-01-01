package com.mrntlu.recyclerviewguide.utils

import androidx.recyclerview.widget.DiffUtil
import com.mrntlu.recyclerviewguide.models.RecyclerViewModel

class RecyclerViewDiffUtilCallBack(
    private val oldList: List<RecyclerViewModel>,
    private val newList: List<RecyclerViewModel>,
): DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        printLog("Are contents the same? ${oldList[oldItemPosition].id} ${oldList[oldItemPosition].id != newList[newItemPosition].id}")
        return when {
            oldList[oldItemPosition].id != newList[newItemPosition].id -> false
            else -> true
        }
    }
}