package com.tigerspike.landmark.presentation.authentication

import android.content.Context
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
import com.tigerspike.landmark.databinding.FragmentSignInBinding
import com.tigerspike.landmark.presentation.MainViewModel
import com.tigerspike.landmark.presentation.ViewState
import com.tigerspike.landmark.util.extension.app
import com.tigerspike.landmark.util.extension.genericErrorAlert
import com.tigerspike.landmark.util.extension.hideKeyboard
import com.tigerspike.landmark.util.extension.loadingAlert
import kotlinx.android.synthetic.main.fragment_search.toolbar
import kotlinx.android.synthetic.main.fragment_sign_in.*
import javax.inject.Inject


class SignInFragment : Fragment() {

    @Inject lateinit var factory: ViewModelProvider.Factory
    private val viewModel: AuthenticationViewModel by viewModels { factory }
    private val activityViewModel: MainViewModel by activityViewModels { factory }
    private lateinit var binding: FragmentSignInBinding
    private var loadingAlert: AlertDialog? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Adding fragment to the dependency graph
        app.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        toolbar.setupWithNavController(findNavController())

        btnSignUp.setOnClickListener {
            findNavController().navigate(SignInFragmentDirections.actionToSignUpFragment())
        }

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