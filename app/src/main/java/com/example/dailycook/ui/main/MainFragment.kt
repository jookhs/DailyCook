package com.example.dailycook.ui.main

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.res.Configuration
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.GridLayoutManager
import com.example.dailycook.R
import com.example.dailycook.databinding.FragmentMainBinding

class MainFragment : Fragment(R.layout.fragment_main) {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var mainAdapter: MainFragmentAdapter
    private var binding: FragmentMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModelFactory = MainViewModelFactory(requireContext())
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
    }

//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        binding = FragmentMainBinding.inflate(inflater, container, false)
//        val view = binding?.root
//        return view
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)
        mainAdapter = MainFragmentAdapter(viewModel.getListOfPantries())
        binding?.rcView?.apply {
            layoutManager = GridLayoutManager(context, if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 4 else 2)
            adapter = mainAdapter
        }
        binding?.textIv?.text = viewModel.remote.toolConfig()?.pantryConfig?.titleText
        binding?.button?.text = viewModel.remote.toolConfig()?.pantryConfig?.recipesButton
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}