package com.mrntlu.recyclerviewguide.adapters.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.mrntlu.recyclerviewguide.databinding.CellErrorBinding
import com.mrntlu.recyclerviewguide.interfaces.Interaction
import com.mrntlu.recyclerviewguide.models.RecyclerViewModel

class ErrorItemViewHolder(
    private val binding: CellErrorBinding,
): RecyclerView.ViewHolder(binding.root), ErrorViewHolderBind<RecyclerViewModel> {
    override fun bind(errorMessage: String?, interaction: Interaction<RecyclerViewModel>) {
        binding.errorText.text = errorMessage

        binding.refreshButton.setOnClickListener { interaction.onErrorRefreshPressed() }
    }
}