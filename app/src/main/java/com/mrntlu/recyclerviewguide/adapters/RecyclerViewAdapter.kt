package com.mrntlu.recyclerviewguide.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.motion.utils.ViewState
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.recyclerview.widget.RecyclerView
import com.mrntlu.recyclerviewguide.adapters.viewholders.*
import com.mrntlu.recyclerviewguide.databinding.*
import com.mrntlu.recyclerviewguide.interfaces.Interaction
import com.mrntlu.recyclerviewguide.models.RecyclerViewModel
import com.mrntlu.recyclerviewguide.utils.RecyclerViewDiffUtilCallBack
import com.mrntlu.recyclerviewguide.utils.RecyclerViewEnum
import com.mrntlu.recyclerviewguide.utils.RVState
import com.mrntlu.recyclerviewguide.utils.printLog

class RecyclerViewAdapter(
    override val interaction: Interaction<RecyclerViewModel>
): BaseAdapter<RecyclerViewModel>(interaction) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            RecyclerViewEnum.View.value -> ItemViewHolder(CellItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            RecyclerViewEnum.Loading.value -> LoadingViewHolder(CellLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            RecyclerViewEnum.PaginationLoading.value -> PaginationLoadingViewHolder(CellPaginationLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            RecyclerViewEnum.PaginationError.value -> PaginationErrorViewHolder(CellPaginationErrorBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            RecyclerViewEnum.Error.value -> ErrorItemViewHolder(CellErrorBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> EmptyViewHolder(CellEmptyBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is ItemViewHolder -> {
                val item = (rvState as RVState.View).list[holder.adapterPosition]

                val text = "Position: ${holder.adapterPosition} ${item.text}"
                holder.binding.itemTV.text = text

                holder.binding.root.setOnClickListener {
                    try {
                        interaction.onItemSelected(
                            holder.adapterPosition,
                            item,
                        )
                    } catch (e: Exception) {
                        Toast.makeText(
                            holder.binding.root.context,
                            "Please wait before doing any operation.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                holder.binding.root.setOnLongClickListener {
                    try {
                        interaction.onLongPressed(
                            holder.adapterPosition,
                            item,
                        )
                    } catch (e: Exception) {
                        Toast.makeText(
                            holder.binding.root.context,
                            "Please wait before doing any operation.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    return@setOnLongClickListener true
                }
            }
            is ErrorItemViewHolder -> {
                holder.binding.errorText.text = (rvState as RVState.Error).message
            }
        }
    }

    override fun handleDiffUtil(newState: RVState<RecyclerViewModel>) {
        if ((newState as RVState.View).list.isEmpty()) {
            rvState = RVState.Empty
            notifyDataSetChanged()
        } else {
            val diffUtil = RecyclerViewDiffUtilCallBack(
                (rvState as RVState.View).list,
                (newState as RVState.View).list,
            )
            val diffResults = DiffUtil.calculateDiff(diffUtil, true)

            (rvState as RVState.View).list = newState.list.toList()

            diffResults.dispatchUpdatesTo(this)
        }
    }
}