package com.github.reline.jisho.radicals

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.reline.jisho.databinding.ItemRadicalBinding
import com.github.reline.jisho.util.publishChannel

data class Radical(
    val value: String,
    val strokes: Int,
    val isSelected: Boolean = false,
    val kanji: List<Char> = emptyList(),
)

class RadicalsAdapter : ListAdapter<Radical, RadicalViewHolder>(DIFF_CALLBACK) {

    val clicks = publishChannel<Radical>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RadicalViewHolder {
        return RadicalViewHolder(ItemRadicalBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: RadicalViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener {
            clicks.offer(item)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Radical>() {
            override fun areItemsTheSame(oldItem: Radical, newItem: Radical): Boolean {
                return oldItem.value == newItem.value
            }

            override fun areContentsTheSame(oldItem: Radical, newItem: Radical): Boolean {
                return oldItem.value == newItem.value && oldItem.isSelected == newItem.isSelected
            }
        }
    }
}

class RadicalViewHolder(private val binding: ItemRadicalBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Radical) {
        binding.radical.text = item.value
        binding.root.setBackgroundColor(if (item.isSelected) Color.GRAY else Color.WHITE)
    }
}
