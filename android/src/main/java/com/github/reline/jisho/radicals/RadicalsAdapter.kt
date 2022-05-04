package com.github.reline.jisho.radicals

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.reline.jisho.databinding.ItemRadicalBinding
import kotlinx.coroutines.channels.Channel

data class Radical(
    val id: Long,
    val value: String,
    val strokes: Int,
    val isSelected: Boolean = false,
    val isEnabled: Boolean = true,
    val kanji: List<Char> = emptyList(),
)

class RadicalsAdapter : ListAdapter<Radical, RadicalViewHolder>(DIFF_CALLBACK) {

    val clicks = Channel<Radical>()

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
            clicks.trySend(item)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Radical>() {
            override fun areItemsTheSame(oldItem: Radical, newItem: Radical): Boolean {
                return oldItem.value == newItem.value
            }

            override fun areContentsTheSame(oldItem: Radical, newItem: Radical): Boolean {
                return oldItem.isEnabled == newItem.isEnabled &&
                        oldItem.isSelected == newItem.isSelected
            }
        }
    }
}

class RadicalViewHolder(private val binding: ItemRadicalBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Radical) {
        binding.radical.text = item.value

        // fixme
        binding.radical.setTextColor(if (item.isEnabled) Color.BLACK else Color.LTGRAY)
        binding.root.isEnabled = item.isEnabled
        binding.radical.isEnabled = item.isEnabled

        binding.root.isSelected = item.isSelected
        binding.root.setBackgroundColor(if (item.isSelected) Color.GRAY else Color.WHITE)
    }
}
