package com.example.android.politicalpreparedness.representative

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import com.example.android.politicalpreparedness.BuildConfig
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.data.network.models.Address
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.example.android.politicalpreparedness.representative.adapter.setNewValue
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.util.*


class DetailFragment : Fragment() {

    companion object {
        //TODO: Add Constant for Location request
        private const val REQUEST_LOCATION_PERMISSION = 1
        private const val REQUEST_TURN_DEVICE_LOCATION_ON = 1002

    }

    //TODO: Declare ViewModel
    private val mViewModel: RepresentativeViewModel by viewModel()
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var mBinding: FragmentRepresentativeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context!!)
        mBinding = FragmentRepresentativeBinding.inflate(inflater)
        mBinding.lifecycleOwner = this
        mBinding.viewModel = mViewModel

        val adapter = RepresentativeListAdapter()
        mBinding.representativesList.adapter = adapter

        mViewModel.myRepresentatives.observe(viewLifecycleOwner) {
            Timber.i("My awesome Reps ${it.size}")
            adapter.submitList(it)
        }

        mViewModel.searchResultsError.observe(viewLifecycleOwner) {
            if (it) {
                Toast.makeText(context, "Error: please enter a valid addres", Toast.LENGTH_LONG)
                    .show()
                mViewModel.searchResultsErrorShown()
            }
        }

        //TODO: Establish button listeners for field and location search
        mBinding.buttonSearch.setOnClickListener {
            mViewModel.fetchMyRepresentativesByFields(
                mBinding.addressLine1.text.toString(),
                mBinding.addressLine2.text.toString(),
                mBinding.city.text.toString(),
                mBinding.state.selectedItem.toString(),
                mBinding.zip.text.toString()
            )
        }

        mBinding.buttonLocation.setOnClickListener {
            getMyLocationWithPermissions()
        }

        return mBinding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_TURN_DEVICE_LOCATION_ON -> {
                getLocation()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //TODO: Handle location permission result to get location on permission granted
        // Check if location permissions are granted and if so enable the
        // location data layer.
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLocation()
            } else {
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    R.string.location_required_error,
                    Snackbar.LENGTH_INDEFINITE
                )
                    .setAction(R.string.settings) {
                        startActivity(Intent().apply {
                            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        })
                    }.show()
            }
        }
    }

    private fun getMyLocationWithPermissions(): Boolean {
        return if (isPermissionGranted()) {
            getLocation()
            true
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            Snackbar.make(
                requireActivity().findViewById(android.R.id.content),
                R.string.location_required_error,
                Snackbar.LENGTH_INDEFINITE
            )
                .setAction(android.R.string.ok) {
                    requestPermissions(
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        REQUEST_LOCATION_PERMISSION
                    )
                }.show()
            false
        } else {
            requestPermissions(
                arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
            false
        }
    }

    private fun isPermissionGranted(): Boolean {
        //TODO: Check if permission is already granted and return (true = granted, false = denied/other)
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getLocation() {
        //TODO: Get location from LocationServices
        //TODO: The geoCodeLocation method is a helper function to change the lat/long location to a human readable street address
        // getting last
        // location from
        // FusedLocationClient
        // object
        // getting last
        // location from
        // FusedLocationClient
        // object
        mFusedLocationClient.getLastLocation()
            .addOnCompleteListener(OnCompleteListener<Location?> { task ->
                val location: Location? = task.getResult()
                if (location != null) {
                    val address = geoCodeLocation(location)
                    mBinding.apply {
                        addressLine1.setText(address.line1)
                        addressLine2.setText(address.line2)
                        city.setText(address.city)
                        state.setNewValue(address.state)
                        zip.setText(address.zip)
                    }
                } else {
                    Timber.i("Location not found error")
                }
            })

    }

    private fun geoCodeLocation(location: Location): Address {
        val geocoder = Geocoder(context, Locale.getDefault())
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
            .map { address ->
                Address(
                    address.thoroughfare,
                    address.subThoroughfare,
                    address.locality,
                    address.adminArea,
                    address.postalCode
                )
            }
            .first()
    }
}