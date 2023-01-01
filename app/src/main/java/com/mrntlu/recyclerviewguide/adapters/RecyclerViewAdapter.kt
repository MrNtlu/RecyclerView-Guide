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
import com.mrntlu.recyclerviewguide.utils.*

@Suppress("UNCHECKED_CAST")
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
                val item = (rvState as DataHolderState<RecyclerViewModel>).list[holder.adapterPosition]

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
            is PaginationErrorViewHolder -> {
                holder.binding.textView3.text = (rvState as RVState.View).paginationErrorMessage
            }
        }
    }

    override fun handleDiffUtil(newState: RVState<RecyclerViewModel>) {
        if (newState is RVState.View && newState.list.isEmpty()) {
            rvState = RVState.Empty
            notifyDataSetChanged()
        } else {
            val diffUtil = RecyclerViewDiffUtilCallBack(
                (rvState as DataHolderState<RecyclerViewModel>).list,
                (newState as DataHolderState<RecyclerViewModel>).list,
            )
            val diffResults = DiffUtil.calculateDiff(diffUtil, true)

            (rvState as DataHolderState<RecyclerViewModel>).list = newState.list.toList()

            diffResults.dispatchUpdatesTo(object: ListUpdateCallback{
                override fun onInserted(position: Int, count: Int) {
                    printLog("Insert $count")
                }

                override fun onRemoved(position: Int, count: Int) {
                    printLog("Removed $count")
                }

                override fun onMoved(fromPosition: Int, toPosition: Int) {
                    printLog("Moved")
                }

                override fun onChanged(position: Int, count: Int, payload: Any?) {
                    printLog("Changed $count")
                }

            })
            diffResults.dispatchUpdatesTo(this)
        }
    }
}