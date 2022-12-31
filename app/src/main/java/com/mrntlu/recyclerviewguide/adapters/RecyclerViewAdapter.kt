package com.mrntlu.recyclerviewguide.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mrntlu.recyclerviewguide.adapters.viewholders.ErrorItemViewHolder
import com.mrntlu.recyclerviewguide.adapters.viewholders.ItemViewHolder
import com.mrntlu.recyclerviewguide.databinding.CellErrorBinding
import com.mrntlu.recyclerviewguide.databinding.CellItemBinding
import com.mrntlu.recyclerviewguide.interfaces.Interaction
import com.mrntlu.recyclerviewguide.models.RecyclerViewModel

class RecyclerViewAdapter(
    override val interaction: Interaction<RecyclerViewModel>
): BaseAdapter<RecyclerViewModel>(interaction) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            ITEM_HOLDER -> ItemViewHolder(CellItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> ErrorItemViewHolder(CellErrorBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is ItemViewHolder -> {
                holder.binding.itemTV.text = arrayList[position].text
            }
            is ErrorItemViewHolder -> {

            }
        }
    }
}