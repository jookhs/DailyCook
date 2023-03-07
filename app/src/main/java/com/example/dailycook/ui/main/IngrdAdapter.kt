package com.example.dailycook.ui.main


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.dailycook.R
import com.example.dailycook.databinding.IngrdItemBinding
import com.example.dailycook.databinding.SpinnerItemBinding
import com.example.dailycook.model.SpinnerItem


class IngrdAdapter(private var items: List<String>?): BaseAdapter() {

    private lateinit var binding: IngrdItemBinding

    override fun getCount(): Int {
       return items?.size ?: 0
    }

    override fun getItem(p0: Int): Any {
        return items?.get(p0).toString()
    }

    override fun getItemId(p0: Int): Long {
       return p0.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        binding = IngrdItemBinding.inflate(LayoutInflater.from(parent?.context), parent, false)
        var view = convertView
        if (view == null) {
            binding = IngrdItemBinding.inflate(LayoutInflater.from(parent?.context), parent, false)
            view = binding.root
        } else binding = view.tag as IngrdItemBinding
        binding.spinnerText.text = items?.get(position)
        binding.spinnerIcon.setImageResource(R.drawable.ic_dot)
        view.tag = binding
        return view
    }
}

