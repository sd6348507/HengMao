package com.roemsoft.hengmao.ui.part_in

import android.view.LayoutInflater
import android.view.ViewGroup
import com.roemsoft.hengmao.R
import com.roemsoft.hengmao.base.BaseListAdapter
import com.roemsoft.hengmao.base.BaseViewHolder
import com.roemsoft.hengmao.base.OnItemChildClickListener
import com.roemsoft.hengmao.bean.CodeData
import com.roemsoft.hengmao.databinding.ItemPartInBinding

class PartInAdapter : BaseListAdapter<CodeData, PartInAdapter.PartInViewHolder>() {

    fun isContainer(code: String): Boolean {
        return list.find { it.barCollectNo.trim() == code.trim() } != null
    }

    fun onDeleteClick(block: (String) -> Unit) {
        onItemChildClick = { view, index ->
            if (view.id == R.id.delete) {
                block.invoke(list[index].barCollectNo)
            }
        }
    }

    fun deleteCode(code: String) {
        val index = list.indexOfFirst { it.barCollectNo == code }
        if (index >= 0) {
            deleteData(index)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartInViewHolder {
        return PartInViewHolder.from(parent)
    }

    override fun convert(holder: PartInViewHolder, item: CodeData?) {
        item ?: return
        holder.bind(item, onItemChildClick)
    }

    class PartInViewHolder private constructor(private val binding: ItemPartInBinding): BaseViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup): PartInViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ItemPartInBinding.inflate(inflater, parent, false)
                val holder = PartInViewHolder(binding)
                return holder
            }
        }

        fun bind(data: CodeData, listener: OnItemChildClickListener?) {
            binding.data = data
            bindOnChildClickListener(binding.delete, listener)
            binding.executePendingBindings()
        }
    }
}