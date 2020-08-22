package com.tigerspike.landmark.presentation.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.tigerspike.landmark.R
import com.tigerspike.landmark.databinding.FragmentSignUpBinding
import com.tigerspike.landmark.presentation.MainViewModel
import com.tigerspike.landmark.presentation.ViewState
import com.tigerspike.landmark.util.extension.genericErrorAlert
import com.tigerspike.landmark.util.extension.hideKeyboard
import com.tigerspike.landmark.util.extension.loadingAlert
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_search.*
import javax.inject.Inject

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private val viewModel: AuthenticationViewModel by viewModels()
    private val activityViewModel: MainViewModel by activityViewModels()
    private lateinit var binding: FragmentSignUpBinding
    private var loadingAlert: AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        toolbar.setupWithNavController(findNavController())

        // observe authentication state and act accordingly
        viewModel.authViewState.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is ViewState.Loading -> loadingAlert = loadingAlert().show()
                is ViewState.Data -> {
                    activityViewModel.updateUserState()
                    loadingAlert?.dismiss()
                    hideKeyboard()
                    findNavController().popBackStack(R.id.mapFragment, false)
                }
                is ViewState.Failure -> {
                    loadingAlert?.dismiss()
                    genericErrorAlert().show()
                }
            }
        })
    }

}