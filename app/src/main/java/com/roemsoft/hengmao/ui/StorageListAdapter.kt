package com.roemsoft.hengmao.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import com.roemsoft.hengmao.base.BaseListAdapter
import com.roemsoft.hengmao.base.BaseViewHolder
import com.roemsoft.hengmao.base.OnItemClickListener
import com.roemsoft.hengmao.bean.Storage
import com.roemsoft.hengmao.databinding.ItemTextSelectedBinding

class StorageListAdapter : BaseListAdapter<Storage, StorageListAdapter.StorageListViewHolder>() {

    private var selected = 0

    override var onItemClick: OnItemClickListener? = { _, index ->
        val o = selected
        selected = index
        notifyItemChanged(o)
        notifyItemChanged(selected)
    }

    fun getSelectedStorage(): String {
        return list[selected].name
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StorageListViewHolder {
        return StorageListViewHolder.from(parent)
    }

    override fun convert(holder: StorageListViewHolder, item: Storage?) {
        item ?: return
        holder.bind(item, selected, onItemClick)
    }

    class StorageListViewHolder private constructor(private val binding: ItemTextSelectedBinding) : BaseViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): StorageListViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binging: ItemTextSelectedBinding = ItemTextSelectedBinding.inflate(layoutInflater, parent, false)
                return StorageListViewHolder(binging)
            }
        }

        // 先set方法设定值，再测量 布局
        // on create view holder 测量值为0
        fun bind(data: Storage, selected: Int, listener: OnItemClickListener?) {
            binding.data = data.name
            binding.isSelected = selected == adapterPosition
            bindOnClickListener(listener)
            binding.executePendingBindings()
        }
    }
}