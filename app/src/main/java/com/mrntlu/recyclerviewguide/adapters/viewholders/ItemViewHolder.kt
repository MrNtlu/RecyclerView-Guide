package com.mrntlu.recyclerviewguide.adapters.viewholders

import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.mrntlu.recyclerviewguide.databinding.CellItemBinding
import com.mrntlu.recyclerviewguide.interfaces.Interaction
import com.mrntlu.recyclerviewguide.models.RecyclerViewModel

class ItemViewHolder(
    private val binding: CellItemBinding,
): RecyclerView.ViewHolder(binding.root), ItemViewHolderBind<RecyclerViewModel> {
    override fun bind(item: RecyclerViewModel, position: Int, interaction: Interaction<RecyclerViewModel>) {
        val text = "Position: $position ${item.text}"
        binding.itemTV.text = item.content.ifBlank { text }

        binding.root.setOnClickListener {
            try {
                interaction.onItemSelected(item)
            } catch (e: Exception) {
                Toast.makeText(
                    binding.root.context,
                    "Please wait before doing any operation.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.root.setOnLongClickListener {
            try {
                interaction.onLongPressed(item)
            } catch (e: Exception) {
                Toast.makeText(
                    binding.root.context,
                    "Please wait before doing any operation.",
                    Toast.LENGTH_SHORT
                ).show()
            }

            return@setOnLongClickListener true
        }
    }
}