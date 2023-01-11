package com.mrntlu.recyclerviewguide.adapters.viewholders

import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mrntlu.recyclerviewguide.R
import com.mrntlu.recyclerviewguide.databinding.CellItemBinding
import com.mrntlu.recyclerviewguide.interfaces.Interaction
import com.mrntlu.recyclerviewguide.models.RecyclerViewModel

class ItemViewHolder(
    private val binding: CellItemBinding,
    private val extraInteraction: RecyclerViewInteraction,
): RecyclerView.ViewHolder(binding.root), ItemViewHolderBind<RecyclerViewModel> {
    override fun bind(item: RecyclerViewModel, position: Int, interaction: Interaction<RecyclerViewModel>) {

        val text = "Position: $position ${item.text}"
        binding.contentTV.text = item.content.ifBlank { text }
        binding.idTV.text = item.id
        binding.favButton.setImageDrawable(ContextCompat.getDrawable(binding.root.context, if (item.isLiked) R.drawable.ic_heart else R.drawable.ic_empty_heart))

        binding.moreButton.setOnClickListener {
            val popupMenu = PopupMenu(binding.root.context, binding.moreButton)
            popupMenu.inflate(R.menu.popup_menu)

            popupMenu.setOnMenuItemClickListener {
                when(it.itemId) {
                    R.id.delete -> {
                        try {
                            extraInteraction.onDeletePressed(item)
                        } catch (e: Exception) {
                            Toast.makeText(
                                binding.root.context,
                                "Please wait before doing any operation.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        return@setOnMenuItemClickListener true
                    }
                    R.id.update -> {
                        try {
                            extraInteraction.onUpdatePressed(item)
                        } catch (e: Exception) {
                            Toast.makeText(
                                binding.root.context,
                                "Please wait before doing any operation.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        return@setOnMenuItemClickListener true
                    }
                    else -> {
                        return@setOnMenuItemClickListener false
                    }
                }
            }

            popupMenu.show()
        }

        binding.favButton.setOnClickListener {
            extraInteraction.onLikePressed(item)
        }

        binding.root.setOnClickListener {
            try {
                interaction.onItemSelected(item, position)
            } catch (e: Exception) {
                Toast.makeText(
                    binding.root.context,
                    "Please wait before doing any operation.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}

interface RecyclerViewInteraction {
    fun onUpdatePressed(item: RecyclerViewModel)
    fun onDeletePressed(item: RecyclerViewModel)
    fun onLikePressed(item: RecyclerViewModel)
}