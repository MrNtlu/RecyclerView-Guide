package com.mrntlu.recyclerviewguide.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.recyclerview.widget.RecyclerView
import com.mrntlu.recyclerviewguide.adapters.viewholders.*
import com.mrntlu.recyclerviewguide.databinding.*
import com.mrntlu.recyclerviewguide.interfaces.Interaction
import com.mrntlu.recyclerviewguide.models.RecyclerViewModel
import com.mrntlu.recyclerviewguide.utils.*

@Suppress("UNCHECKED_CAST")
class RecyclerViewAdapter(
    override val interaction: Interaction<RecyclerViewModel>,
    private val extraInteraction: RecyclerViewInteraction,
): BaseAdapter<RecyclerViewModel>(interaction) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            RecyclerViewEnum.View.value -> ItemViewHolder(CellItemBinding.inflate(LayoutInflater.from(parent.context), parent, false), extraInteraction)
            RecyclerViewEnum.Loading.value -> LoadingViewHolder(CellLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            RecyclerViewEnum.PaginationLoading.value -> PaginationLoadingViewHolder(CellPaginationLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            RecyclerViewEnum.PaginationExhaust.value -> PaginationExhaustViewHolder(CellPaginationExhaustBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            RecyclerViewEnum.Error.value -> ErrorItemViewHolder(CellErrorBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> EmptyViewHolder(CellEmptyBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun handleDiffUtil(newList: ArrayList<RecyclerViewModel>) {
        val diffUtil = RecyclerViewDiffUtilCallBack(
            arrayList,
            newList,
        )
        val diffResults = DiffUtil.calculateDiff(diffUtil, true)

        arrayList = newList.toList() as ArrayList<RecyclerViewModel>

        diffResults.dispatchUpdatesTo(object: ListUpdateCallback{ //For logging purposes only, it's not necessary
            override fun onInserted(position: Int, count: Int) {
                printLog("Insert $count")
            }

            override fun onRemoved(position: Int, count: Int) {
                printLog("Removed $count")
            }

            override fun onMoved(fromPosition: Int, toPosition: Int) {
                printLog("Moved $fromPosition $toPosition")
            }

            override fun onChanged(position: Int, count: Int, payload: Any?) {
                printLog("Changed $position $count")
            }

        })
        diffResults.dispatchUpdatesTo(this)
    }
}