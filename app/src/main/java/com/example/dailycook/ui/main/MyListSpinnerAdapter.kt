package com.example.dailycook.ui.main


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.dailycook.R
import com.example.dailycook.databinding.SpinnerItemBinding
import com.example.dailycook.model.SpinnerItem


class MyListSpinnerAdapter(private var items: MutableList<SpinnerItem>): BaseAdapter() {

    private lateinit var binding: SpinnerItemBinding
    private var tempList = items.toMutableList()

    override fun getCount(): Int {
       return items.size
    }

    override fun getItem(p0: Int): Any {
        return items[p0]
    }

    override fun getItemId(p0: Int): Long {
       return 0
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        binding = SpinnerItemBinding.inflate(LayoutInflater.from(parent?.context), parent, false)
        var view = convertView
        if (view == null) {
            binding = SpinnerItemBinding.inflate(LayoutInflater.from(parent?.context), parent, false)
            view = binding.root
        } else binding = view.tag as SpinnerItemBinding
        binding.spinnerText.text = items[position].text
        binding.spinnerIcon.setImageResource(if (items[position].added) R.drawable.ic_delete else R.drawable.add_icon)
        view.tag = binding
        return view
    }

    fun setItemsList(spinnerItem: SpinnerItem) {
       tempList.find { it.text == spinnerItem.text }?.added = spinnerItem.added
    }

    fun filter(spinnerItems: List<SpinnerItem>?) {
        items.clear()
        if (spinnerItems == null || spinnerItems.isEmpty()) {
            items.addAll(tempList)
        } else {
            tempList.forEach {
                spinnerItems.forEach { spinnerItem ->
                    if (it.text == spinnerItem.text) {
                        items.add(it)
                    }
                }
            }
        }
        notifyDataSetChanged()
    }
}

