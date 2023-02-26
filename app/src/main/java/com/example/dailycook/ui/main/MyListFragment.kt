package com.example.dailycook.ui.main

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.dailycook.MainAdapterClickListener
import com.example.dailycook.R
import com.example.dailycook.databinding.FragmentMyListBinding
import com.example.dailycook.model.SpinnerItem
import java.util.*

class MyListFragment: Fragment(R.layout.fragment_my_list), MainAdapterClickListener {
    companion object {
        fun newInstance() = MyListFragment()
    }

    private val viewModel: MainViewModel by activityViewModels {
        MainViewModelFactory(requireContext())
    }
    private lateinit var myListAdapter: MyListAdapter
    private lateinit var myListSpinnerAdapter: MyListSpinnerAdapter
    private var binding: FragmentMyListBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentMyListBinding.bind(view)

        if (savedInstanceState != null) {
            viewModel.currentPantryName = savedInstanceState.getString("currentPantryName")
            viewModel.myIngredientsList = savedInstanceState.getStringArrayList("ingredientsList")?.toMutableList() ?: emptyList<String>().toMutableList()
            viewModel.addToMyList(viewModel.myIngredientsList)
        }
        myListSpinnerAdapter =
            MyListSpinnerAdapter(viewModel.getSpinnerItems())
        myListAdapter = MyListAdapter(requireContext(), this)
        binding?.myListRcView?.apply {
            layoutManager = GridLayoutManager(context, 1)
            adapter = myListAdapter
        }
        viewModel.updateMyListState()
        binding?.myListButton?.text = viewModel.remote.toolConfig()?.pantryConfig?.recipesButton
        setupMyListSearch()
        binding?.btnBackMyList?.setOnClickListener {
            viewModel.updateMyListState()
            onBackPressed()
        }
        myListAdapter.setItems(viewModel.getMyPantryItems())
        myListAdapter.notifyDataSetChanged()
        updateUI()
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (onBackPressed()) activity?.onBackPressed()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("currentPantryName", viewModel.currentPantryName)
        outState.putStringArrayList("ingredientsList", viewModel.myIngredientsList as? ArrayList<String>)
    }

    private fun updateUI() {
        binding?.noItems?.visibility = if (myListAdapter.itemCount == 0) View.VISIBLE else View.GONE
        binding?.myListTitle?.text = viewModel.remote.toolConfig()?.myPantry?.title
        binding?.noItemText?.text = viewModel.remote.toolConfig()?.myPantry?.noItems
        binding?.myListSubTitle?.text = String.format(viewModel.remote.toolConfig()?.myPantry?.subTitle ?: "", viewModel.myIngredientsList.size)
        binding?.myListButton?.isEnabled = viewModel.myIngredientsList.isNotEmpty()
        binding?.myListButton?.setBackgroundColor(if (binding?.myListButton?.isEnabled == true) resources.getColor(R.color.teal_new) else resources.getColor(R.color.disabled))
    }

    override fun onItemClicked(item: String) {
        binding?.noItems?.visibility = if (myListAdapter.itemCount == 0) View.VISIBLE else View.GONE
        viewModel.deleteFromMyList(item)
        viewModel.updateMyListState()
        binding?.myListSubTitle?.text = String.format(
            viewModel.remote.toolConfig()?.myPantry?.subTitle ?: "",
            viewModel.myList.value?.size ?: 0
        )
    }

    private fun setupMyListSearch() {
        binding?.mySearchList?.adapter = myListSpinnerAdapter
        binding?.mySearchList?.setOnItemClickListener { adapterView, view, position, l ->
            val item = myListSpinnerAdapter.getItem(position) as SpinnerItem
            if (viewModel.myIngredientsList.contains(item.text)) {
                viewModel.spinnerItemsList[position].added = false
                myListSpinnerAdapter.setItemsList(SpinnerItem(item.text, false))
                viewModel.deleteFromMyList(item.text)
                Toast.makeText(requireContext(), item.text + "removed", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.spinnerItemsList[position].added = true
                myListSpinnerAdapter.setItemsList(SpinnerItem(item.text, true))
                viewModel.addToMyList(listOf(item.text))
                Toast.makeText(requireContext(), item.text + "added", Toast.LENGTH_SHORT).show()
            }
            viewModel.updateMyListState()
            viewModel.updateSpinnerItems()
            myListAdapter.setItems(viewModel.getMyPantryItems())
            myListSpinnerAdapter.notifyDataSetChanged()
            myListAdapter.notifyDataSetChanged()
            updateUI()
            binding?.mySearchList?.visibility = View.GONE
        }
        binding?.myListSearch?.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding?.mySearchList?.visibility = View.GONE
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null && newText != "") {
                    binding?.mySearchList?.visibility = View.VISIBLE
                    val list = mutableListOf<SpinnerItem>()
                    viewModel.spinnerItemsList.forEach {
                        if (it.text.startsWith(newText.replaceFirstChar { char ->
                                if (char.isLowerCase()) char.titlecase(
                                    Locale.getDefault()
                                ) else char.toString()
                            }) || it.text.contains(newText)) {
                            if (list.none { listItem -> listItem.text == it.text }) list.add(it)
                        }
                    }
                    if (list.isNotEmpty()) {
                        myListSpinnerAdapter.filter(list)
                    } else {
                        binding?.mySearchList?.visibility = View.GONE
                    }
                } else {
                    myListSpinnerAdapter.filter(viewModel.spinnerItemsList)
                    binding?.mySearchList?.visibility = View.GONE
                }
                return false
            }
        })
    }

    private fun hide() {
        activity ?: return
        parentFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
            .hide(this)
            .commitAllowingStateLoss()
    }

    fun onBackPressed(): Boolean {
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.container, MainFragment.newInstance())
            .commitNow()
        hide()
        return false
    }
}