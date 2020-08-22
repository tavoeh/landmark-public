package com.tigerspike.landmark.presentation.notes

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.tigerspike.landmark.R
import com.tigerspike.landmark.presentation.MainViewModel
import com.tigerspike.landmark.presentation.ViewState
import com.tigerspike.landmark.util.Event
import com.tigerspike.landmark.util.extension.*
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_save_note.*
import kotlinx.android.synthetic.main.fragment_search.toolbar
import javax.inject.Inject

/**
 * Fragment displays the save notes screen
 */
@AndroidEntryPoint
class SaveNoteFragment : Fragment() {

    private val args: SaveNoteFragmentArgs by navArgs()

    private val viewModel: SaveNoteViewModel by viewModels()
    private val activityViewModel: MainViewModel by activityViewModels()

    private var loadingAlert: AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_save_note, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        toolbar.setupWithNavController(findNavController())
        toolbar.inflateMenu(R.menu.save_note_menu)
        toolbar.setOnMenuItemClickListener {
            viewModel.saveNote(txtText.text.toString(), args.location)
            true
        }

        txtText.requestFocus()
        showKeyboard()

        // receive the on save note event from the viewModel and act accordingly
        viewModel.onSaveNoteEvent.observe(viewLifecycleOwner, Event.Observer { state ->
            when (state) {
                is ViewState.Loading -> loadingAlert = loadingAlert().show()
                is ViewState.Data -> {
                    loadingAlert?.dismiss()
                    activityViewModel.onSaveNote()
                    hideKeyboard()
                    findNavController().navigateUp()
                }
                is ViewState.Failure -> {
                    loadingAlert?.dismiss()
                    genericErrorAlert().show()
                }
            }
        })
    }
}