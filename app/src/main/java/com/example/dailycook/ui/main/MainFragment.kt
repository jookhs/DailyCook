package com.example.dailycook.ui.main

import android.content.res.ColorStateList
import android.content.res.Configuration
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.dailycook.MainAdapterClickListener
import com.example.dailycook.R
import com.example.dailycook.databinding.FragmentMainBinding
import com.example.dailycook.model.MyListItem
import com.example.dailycook.model.PantryItem
import com.facebook.common.util.UriUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.chip.Chip
import java.util.*

class MainFragment : Fragment(R.layout.fragment_main), MainAdapterClickListener {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var mainAdapter: MainFragmentAdapter
    private lateinit var myListAdapter: MyListAdapter
    private var binding: FragmentMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModelFactory = MainViewModelFactory(requireContext())
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)
        if (savedInstanceState != null) {
            binding?.pantryPreview?.visibility = savedInstanceState.getInt("previewVisibility")
            binding?.myListLayout?.visibility = savedInstanceState.getInt("myListVisibility")
            viewModel.currentPantryName = savedInstanceState.getString("currentPantryName")
            viewModel.ingredientsList = savedInstanceState.getStringArrayList("ingredientsList")?.toMutableList() ?: emptyList<String>().toMutableList()
            viewModel.addToMyList(viewModel.ingredientsList)
        }
        myListAdapter = MyListAdapter(requireContext(), this)
        binding?.myListRcView?.apply {
            layoutManager = GridLayoutManager(context, 1)
            adapter = myListAdapter
        }
        mainAdapter = MainFragmentAdapter(viewModel.getListOfPantries(resources, context), this)
        binding?.rcView?.apply {
            layoutManager = GridLayoutManager(context, if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 4 else 2)
            adapter = mainAdapter
        }
        if (binding?.pantryPreview?.visibility == View.VISIBLE && viewModel.currentPantryName != null) {
            val pantryImage = viewModel.remote.toolConfig()?.pantryConfig?.products?.find { it.name == viewModel.currentPantryName }?.icon ?: ""
            val icon = resources.getIdentifier(pantryImage, "drawable", context?.packageName)
            val uri = Uri.Builder()
                .scheme(UriUtil.LOCAL_RESOURCE_SCHEME)
                .path(java.lang.String.valueOf(icon))
                .build()
            addIngredientsToChipList(PantryItem(uri, viewModel.currentPantryName ?: ""))
            showPreview()
        }
        binding?.text1?.text = viewModel.remote.toolConfig()?.pantryConfig?.titleText1
        binding?.text2?.text = viewModel.remote.toolConfig()?.pantryConfig?.titleText2
        binding?.button?.text = viewModel.remote.toolConfig()?.pantryConfig?.recipesButton
        binding?.myListButton?.text = viewModel.remote.toolConfig()?.pantryConfig?.recipesButton
        binding?.search?.queryHint = viewModel.remote.toolConfig()?.pantryConfig?.searchHint
        binding?.pantryButton?.text = viewModel.remote.toolConfig()?.pantryConfig?.applyBtn
        binding?.myListBadge?.visibility = if (viewModel.myList.value?.isNotEmpty() == true) View.VISIBLE else View.GONE
        binding?.btnBack?.setOnClickListener {
            viewModel.updatePantryListState()
            mainAdapter.notifyDataSetChanged()
            hidePreview()
            hideMyList()
        }

        binding?.btnBackMyList?.setOnClickListener {
            viewModel.updateMyListState()
            mainAdapter.notifyDataSetChanged()
            hideMyList()
        }
        updateMainButtonState()
        binding?.pantryButton?.setOnClickListener {
            viewModel.updatePantryListState()
            mainAdapter.notifyDataSetChanged()
            hidePreview()
        }
        viewModel.myList.observe(viewLifecycleOwner, Observer {
            binding?.myListBadge?.visibility = if (it.isNotEmpty()) View.VISIBLE else View.GONE
            updateMainButtonState()
        })

        binding?.myList?.setOnClickListener {
            openMyList()
        }
        if (binding?.myListLayout?.visibility == View.VISIBLE) {
            openMyList()
        }

        val sheetBehavior = BottomSheetBehavior.from(binding?.contentLayout ?: view)
        sheetBehavior.isFitToContents = true
        sheetBehavior.isHideable = false
        sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        view.viewTreeObserver.addOnGlobalLayoutListener {
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                sheetBehavior.state = BottomSheetBehavior.STATE_DRAGGING
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("previewVisibility", binding?.pantryPreview?.visibility ?: View.GONE)
        outState.putInt("myListVisibility", binding?.myListLayout?.visibility ?: View.GONE)
        outState.putString("currentPantryName", viewModel.currentPantryName)
        outState.putStringArrayList("ingredientsList", viewModel.ingredientsList as? ArrayList<String>)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onImageClicked(pantryItem: PantryItem?) {
        addIngredientsToChipList(pantryItem)
        showPreview()
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

    private fun updateMainButtonState() {
        binding?.button?.isEnabled = viewModel.ingredientsList.isNotEmpty()
        binding?.myListButton?.isEnabled = viewModel.ingredientsList.isNotEmpty()
        binding?.button?.setBackgroundColor(if (binding?.button?.isEnabled == true) resources.getColor(R.color.teal_new) else resources.getColor(R.color.disabled))
        binding?.myListButton?.setBackgroundColor(if (binding?.myListButton?.isEnabled == true) resources.getColor(R.color.teal_new) else resources.getColor(R.color.disabled))
    }

    private fun showPreview() {
        binding?.myList?.visibility = View.GONE
        binding?.button?.visibility = View.GONE
        binding?.pantryPreview?.visibility = View.VISIBLE
        binding?.pantryButton?.visibility = View.VISIBLE
    }

    private fun hidePreview() {
        viewModel.currentPantryName = null
        binding?.myList?.visibility = View.VISIBLE
        binding?.button?.visibility = View.VISIBLE
        binding?.pantryPreview?.visibility = View.GONE
        binding?.pantryButton?.visibility = View.GONE
        binding?.chips?.removeAllViews()
    }

    private fun hideMyList() {
        binding?.myListLayout?.visibility = View.GONE
        binding?.rcView?.visibility = View.VISIBLE
        binding?.button?.visibility = View.VISIBLE
    }

    private fun addIngredientsToChipList(pantryItem: PantryItem?) {
        viewModel.checkedChipsList.clear()
        binding?.pantryIcon?.setImageURI(pantryItem?.image)
        binding?.pantryTitle?.text = pantryItem?.title
        viewModel.currentPantryName = pantryItem?.title
        viewModel.remote.toolConfig()?.pantryConfig?.products?.find { it.name == pantryItem?.title }?.set?.forEach {
            val chip = Chip(context)
            val text = it.replaceFirstChar{ char -> if (char.isLowerCase()) char.titlecase(Locale.getDefault()) else char.toString() }
            chip.text = text
            chip.isCloseIconVisible = false
            chip.isClickable = true
            chip.isCheckable = true
            context?.let { it1 ->
                chip.chipBackgroundColor = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        it1, R.color.disabled
                    )
                )
            }
            binding?.chips?.addView(chip)
            if (viewModel.ingredientsList.contains(it)) {
                viewModel.addChip(it)
                viewModel.addToMyList(viewModel.checkedChipsList)
                chip.isChecked = true
                context?.let { it1 ->
                    chip.chipBackgroundColor = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            it1, R.color.teal_new
                        )
                    )
                }
            }
            chip.setOnClickListener { ch ->
                if (chip.isChecked) {
                    viewModel.addChip(it)
                    viewModel.addToMyList(viewModel.checkedChipsList)
                    context?.let { it1 ->
                        chip.chipBackgroundColor = ColorStateList.valueOf(
                            ContextCompat.getColor(
                                it1, R.color.teal_new
                            )
                        )
                    }
                } else {
                    viewModel.removeChip(it)
                    viewModel.deleteFromMyList(it)
                    context?.let { it1 ->
                        chip.chipBackgroundColor = ColorStateList.valueOf(
                            ContextCompat.getColor(
                                it1, R.color.disabled
                            )
                        )
                    }
                }
            }
        }
    }

    private fun openMyList() {
        val list = viewModel.getMyPantryItems()
        myListAdapter.setItems(list)
        myListAdapter.notifyDataSetChanged()
        binding?.noItems?.visibility = if (myListAdapter.itemCount == 0) View.VISIBLE else View.GONE
        binding?.myListTitle?.text = viewModel.remote.toolConfig()?.myPantry?.title
        binding?.noItemText?.text = viewModel.remote.toolConfig()?.myPantry?.noItems
        binding?.myListSubTitle?.text = String.format(viewModel.remote.toolConfig()?.myPantry?.subTitle ?: "", viewModel.myList.value?.size ?: 0)
        binding?.myListLayout?.visibility = View.VISIBLE
        binding?.rcView?.visibility = View.GONE
        binding?.button?.visibility = View.GONE
    }

}