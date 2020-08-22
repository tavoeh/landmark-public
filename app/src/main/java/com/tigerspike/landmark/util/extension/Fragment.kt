package com.tigerspike.landmark.util.extension

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tigerspike.landmark.LandMarkApp
import com.tigerspike.landmark.R

/**
 * Created by Gustavo Enriquez on 25/7/20.
 *
 * Extension functions that offer extra functionality to the Fragment class
 **/

val Fragment.app: LandMarkApp
    get() = requireActivity().application as LandMarkApp

fun Fragment.showKeyboard() {
    try {
        (requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .showSoftInput(requireActivity().currentFocus, 0)
    } catch (e: Exception) {
        // Nothing to do here
    }
}

fun Fragment.hideKeyboard() {
    try {
        (requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
    } catch (e: Exception) {
        //Nothing to do here
    }
}

// region Alerts
fun Fragment.genericErrorAlert(): MaterialAlertDialogBuilder =
    MaterialAlertDialogBuilder(requireContext())
        .setTitle(getString(R.string.error_generic_message))
        .setPositiveButton(resources.getString(R.string.ok)) { dialog, _ -> dialog.dismiss() }

fun Fragment.actionAlert(title: String, actionLabel: String, action: () -> Unit): MaterialAlertDialogBuilder =
    MaterialAlertDialogBuilder(requireContext())
        .setTitle(title)
        .setPositiveButton(actionLabel) { dialog, _ ->
            dialog.cancel()
            dialog.dismiss()
            action()
        }

fun Fragment.loadingAlert(): MaterialAlertDialogBuilder =
    MaterialAlertDialogBuilder(requireContext())
        .setMessage(getString(R.string.loading))
        .setCancelable(false)

//endregion
