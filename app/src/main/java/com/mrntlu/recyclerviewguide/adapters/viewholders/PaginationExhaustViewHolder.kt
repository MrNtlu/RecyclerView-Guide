package com.mrntlu.recyclerviewguide.adapters.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.mrntlu.recyclerviewguide.databinding.CellPaginationExhaustBinding
import com.mrntlu.recyclerviewguide.interfaces.Interaction
import com.mrntlu.recyclerviewguide.models.RecyclerViewModel

class PaginationExhaustViewHolder(
    private val binding: CellPaginationExhaustBinding,
): RecyclerView.ViewHolder(binding.root), PaginationExhaustViewHolderBind<RecyclerViewModel> {
    override fun bind(interaction: Interaction<RecyclerViewModel>) {
        binding.topButton.setOnClickListener { interaction.onExhaustButtonPressed() }
    }
}