package com.tigerspike.landmark.presentation.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tigerspike.landmark.databinding.FragmentSearchBinding
import com.tigerspike.landmark.presentation.ViewState
import com.tigerspike.landmark.util.extension.genericErrorAlert
import com.tigerspike.landmark.util.extension.showKeyboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_search.*

/**
 * Fragment displays the search notes screen
 */
@AndroidEntryPoint
class SearchFragment : Fragment() {

    private val viewModel: SearchViewModel by viewModels()
    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: SearchResultAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        toolbar.setupWithNavController(findNavController())
        txtQuery.requestFocus()
        showKeyboard()

        adapter = SearchResultAdapter()
        binding.rvNotes.adapter = adapter
        binding.rvNotes.layoutManager = LinearLayoutManager(context)

        viewModel.notesViewState.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is ViewState.Data -> adapter.submitList(state.data)
                is ViewState.Failure -> genericErrorAlert().show()
            }
        })
    }
}