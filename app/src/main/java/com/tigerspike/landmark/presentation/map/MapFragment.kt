package com.tigerspike.landmark.presentation.map

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.tigerspike.landmark.R
import com.tigerspike.landmark.domain.model.User
import com.tigerspike.landmark.presentation.MainViewModel
import com.tigerspike.landmark.presentation.ViewState
import com.tigerspike.landmark.util.Event
import com.tigerspike.landmark.util.extension.actionAlert
import com.tigerspike.landmark.util.extension.app
import com.tigerspike.landmark.util.extension.genericErrorAlert
import kotlinx.android.synthetic.main.fragment_map.*
import javax.inject.Inject

/**
 * Fragment displays the notes on a Map
 */
class MapFragment : Fragment() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val viewModel: MapViewModel by viewModels { factory }
    private val activityViewModel: MainViewModel by activityViewModels { factory }

    private lateinit var map: GoogleMap
    private var mapState: MapViewModel.MapState? = null
    private var userState: User = User.Guest

    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    private lateinit var newNoteBottomSheet: BottomSheetBehavior<ConstraintLayout>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // adding fragment to the dependency graph
        app.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_map, container, false)

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        newNoteBottomSheet = BottomSheetBehavior.from(bsNewNote)
        newNoteBottomSheet.state = BottomSheetBehavior.STATE_HIDDEN

        // Navigate to the search screen
        btnSearch.setOnClickListener {
            findNavController().navigate(MapFragmentDirections.actionToSearchFragment())
            viewModel.setSelectedNoteLocation(null)
        }

        // refresh notes
        btnRefresh.setOnClickListener {
            viewModel.getNotes()
            viewModel.setSelectedNoteLocation(null)
            Toast.makeText(context, R.string.refreshing, Toast.LENGTH_SHORT).show()
        }

        // Navigate to the settings screen
        btnSettings.setOnClickListener {
            findNavController().navigate(MapFragmentDirections.actionToSettingsFragment())
            viewModel.setSelectedNoteLocation(null)
        }

        // Close Bottom sheet
        btnCloseNewNote.setOnClickListener {
            viewModel.setSelectedNoteLocation(null)
        }

        // Add New note at user's location
        btnNewNoteAtMyLocation.setOnClickListener {
            requestLocationPermissionIfNeeded()
        }

        // Add new note only if user is authenticated
        btnAddNote.setOnClickListener {
            when (userState) {
                is User.Authenticated -> {
                    mapState?.selectedNoteLocation?.let { location ->
                        findNavController().navigate(MapFragmentDirections.actionToAddNoteFragment(location))
                    }
                }
                else -> actionAlert(
                    title = getString(R.string.authentication_need_sign_in),
                    actionLabel = getString(R.string.authentication_sign_in),
                    action = {
                        findNavController().navigate(MapFragmentDirections.actionToSignInFragment())
                        viewModel.setSelectedNoteLocation(null)
                    }
                ).show()
            }
        }

        // Get Note when Map Fragment view gets created
        // but observe the result only when the map is ready
        viewModel.getNotes()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activityViewModel.onSaveNoteEvent.observe(viewLifecycleOwner, Event.Observer {
            viewModel.setSelectedNoteLocation(null)
            // Re-fetch notes when a new note is created
            viewModel.getNotes()
        })

        // Observe user states
        activityViewModel.userState.observe(viewLifecycleOwner, Observer { user ->
            userState = user
        })
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     */
    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap

        map.setOnCameraIdleListener {
            viewModel.setMapLastLocation(map.cameraPosition.target)
        }

        map.setOnMapClickListener { location ->
            viewModel.setSelectedNoteLocation(location)
        }

        map.setOnMarkerClickListener {
            it.showInfoWindow()
            viewModel.setSelectedNoteLocation(null)
            true
        }

        map.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(MAP_DEFAULT_POSITION_LAT, MAP_DEFAULT_POSITION_LNG),
                MAP_DEFAULT_ZOOM
            )
        )

        map.isMyLocationEnabled = isLocationPermissionGranted

        // display the 'add new note at my location' button
        // only when the map is ready
        btnNewNoteAtMyLocation.visibility = View.VISIBLE

        viewModel.mapViewState.observe(viewLifecycleOwner, Observer { mapViewState ->
            handleState(mapViewState)
        })

    }

    /**
     * Handles updates to the map states
     */
    private fun handleState(mapViewState: ViewState<MapViewModel.MapState>) {
        when (mapViewState) {
            is ViewState.Data -> {

                // Display markers on the map and add new notes only
                val newNotes = mapViewState.data.notes.subtract(mapState?.notes ?: listOf())
                newNotes.forEach { note -> addNoteMarker(note.location, note.text, note.color) }

                // Move map to the last position
                mapViewState.data.mapLastLocation?.let { location ->
                    map.moveCamera(CameraUpdateFactory.newLatLng(location))
                }

                // Clear selected marker if any
                clearSelectedNoteMarker()

                // Add selected marker
                mapViewState.data.selectedNoteLocation?.let { location ->
                    viewModel.selectedNoteMarker = addNoteMarker(location, "", BitmapDescriptorFactory.HUE_CYAN)
                    map.animateCamera(CameraUpdateFactory.newLatLng(location), MAP_ANIMATION_TIME, null)
                    showNewNoteBottomSheet(location = location, isUserLocation = mapViewState.data.isUserLocation)
                }

                // Updating the local mapState
                mapState = mapViewState.data.copy()
            }
            is ViewState.Failure -> genericErrorAlert().show()
        }
    }

    /**
     * Method gets user's last location and select a Note location
     * This method should be called only if location permission has been granted
     */
    @SuppressLint("MissingPermission")
    private fun addNoteAtMyLocation() {
        map.isMyLocationEnabled = isLocationPermissionGranted
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            when (location != null) {
                true -> {
                    val lastLatLng = LatLng(location.latitude, location.longitude)
                    viewModel.setSelectedNoteLocation(location = lastLatLng, isUserLocation = true)
                }
                false -> genericErrorAlert().show()
            }
        }
    }

    private fun addNoteMarker(location: LatLng, text: String, color: Float = BitmapDescriptorFactory.HUE_AZURE): Marker =
        map.addMarker(
            MarkerOptions()
                .title(text)
                .position(location)
                .icon(BitmapDescriptorFactory.defaultMarker(color))

        )

    private fun showNewNoteBottomSheet(isUserLocation: Boolean = false, location: LatLng) {
        txtTitle.text = getString(
            when (isUserLocation) {
                true -> R.string.new_note_at_my_location
                false -> R.string.new_note_at_location
            }
        )

        txtLocation.text = getString(R.string.lat_lng_location, location.latitude.toString(), location.longitude.toString())
        newNoteBottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun clearSelectedNoteMarker() {
        viewModel.selectedNoteMarker?.remove()
        viewModel.selectedNoteMarker = null
        newNoteBottomSheet.state = BottomSheetBehavior.STATE_HIDDEN
    }

    //region Permissions management
    private fun requestLocationPermissionIfNeeded() {
        when (isLocationPermissionGranted) {
            true -> addNoteAtMyLocation()
            false -> requestPermissions(arrayOf(ACCESS_FINE_LOCATION), REQUEST_LOCATION)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 1) {
            when (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                true -> addNoteAtMyLocation()
                false -> Snackbar.make(container, getString(R.string.enable_location_services), Snackbar.LENGTH_LONG).show()
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private val isLocationPermissionGranted
        get() = ContextCompat.checkSelfPermission(requireContext(), ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    //endregion

    //Constants
    companion object {
        const val REQUEST_LOCATION = 1
        const val MAP_DEFAULT_ZOOM = 14.0f
        const val MAP_ANIMATION_TIME = 300

        //Melbourne
        const val MAP_DEFAULT_POSITION_LAT = -37.8124517
        const val MAP_DEFAULT_POSITION_LNG = 144.9595111
    }
}