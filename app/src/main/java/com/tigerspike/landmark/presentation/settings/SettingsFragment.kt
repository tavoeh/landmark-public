package com.tigerspike.landmark.presentation.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.tigerspike.landmark.R
import com.tigerspike.landmark.domain.model.User
import com.tigerspike.landmark.presentation.MainViewModel
import com.tigerspike.landmark.presentation.ViewState
import com.tigerspike.landmark.util.extension.app
import com.tigerspike.landmark.util.extension.genericErrorAlert
import kotlinx.android.synthetic.main.fragment_settings.*
import javax.inject.Inject

/**
 * Fragment for various configuration aspects of the system
 */
class SettingsFragment : Fragment() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val viewModel: SettingsViewModel by viewModels { factory }
    private val activityViewModel: MainViewModel by activityViewModels { factory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // adding fragment to the dependency graph
        app.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_settings, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        toolbar.setupWithNavController(findNavController())

        btnSignIn.setOnClickListener {
            findNavController().navigate(SettingsFragmentDirections.actionToSignInFragment())
        }

        btnSignOut.setOnClickListener {
            viewModel.signOut()
        }

        viewModel.viewState.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is ViewState.Data -> activityViewModel.updateUserState()
                is ViewState.Failure -> genericErrorAlert().show()
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Get the the user state and display the components accordingly
        activityViewModel.userState.observe(viewLifecycleOwner, Observer { user ->
            when (user) {
                is User.Authenticated -> {
                    userName.text = user.name
                    btnSignOut.visibility = View.VISIBLE
                    btnSignIn.visibility = View.GONE
                }
                else -> {
                    userName.text = null
                    btnSignOut.visibility = View.GONE
                    btnSignIn.visibility = View.VISIBLE
                }
            }
        })
    }

}