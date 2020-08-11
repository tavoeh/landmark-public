package com.tigerspike.landmark.presentation.search

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tigerspike.landmark.databinding.FragmentSearchBinding
import com.tigerspike.landmark.presentation.ViewState
import com.tigerspike.landmark.util.extension.app
import com.tigerspike.landmark.util.extension.genericErrorAlert
import com.tigerspike.landmark.util.extension.showKeyboard
import kotlinx.android.synthetic.main.fragment_search.*
import javax.inject.Inject

/**
 * Fragment displays the search notes screen
 */
class SearchFragment : Fragment() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val viewModel: SearchViewModel by viewModels { factory }

    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: SearchResultAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // adding fragment to the dependency graph
        app.appComponent.inject(this)
    }

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