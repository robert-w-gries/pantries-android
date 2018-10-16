package org.endhungerdurham.pantries

import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng

private val DEFAULT_ZOOM = 10.0f
private val DURHAM_NC: LatLng = LatLng(35.9940, -78.8986)
private val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1

private val KEY_CAMERA_POSITION = "camera_position"
private val KEY_LOCATION = "location"

class MapFragment : Fragment() {
    private var mFusedLocationProviderClient: FusedLocationProviderClient ?= null
    private var mLastLocation: LatLng ?= null
    private var mLocationPermissionGranted: Boolean = false
    private var mMap: GoogleMap ?= null
    private var mMapView: MapView ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            mLastLocation = savedInstanceState.getParcelable(KEY_LOCATION)
            mMap?.moveCamera(CameraUpdateFactory.newCameraPosition(savedInstanceState.getParcelable(KEY_CAMERA_POSITION)))
        }

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_map, container, false)

        mMapView = rootView.findViewById(R.id.mapView)
        mMapView?.onCreate(savedInstanceState)
        mMapView?.onResume()

        mMapView?.getMapAsync{
            mMap = it
            updateMyLocationUI()
            if (mLastLocation == null) {
                getDeviceLocation()
            }
        }

        return rootView
    }

    private fun getDeviceLocation() {
        if (mLocationPermissionGranted) {
            try {
                mFusedLocationProviderClient?.lastLocation?.addOnSuccessListener {
                    it?.let { loc ->
                        updateCamera(LatLng(loc.latitude, loc.longitude))
                    }
                }?.addOnFailureListener {
                    updateCamera(DURHAM_NC)
                }
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
        } else {
            updateCamera(DURHAM_NC)
        }
    }

    private fun getLocationPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(requireContext(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true
            return true
        } else {
            requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
            return false
        }
    }

    private fun updateCamera(pos: LatLng) {
        mLastLocation = pos
        mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, DEFAULT_ZOOM))
    }

    private fun updateMyLocationUI() {
        try {
            if (mLocationPermissionGranted || getLocationPermission()) {
                mMap?.isMyLocationEnabled = true
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    mLocationPermissionGranted = true
                    updateMyLocationUI()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mMapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mMapView?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMapView?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMapView?.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        mMap?.let {
            outState.putParcelable(KEY_CAMERA_POSITION, it.cameraPosition)
            outState.putParcelable(KEY_LOCATION, mLastLocation)
        }
        super.onSaveInstanceState(outState)
        mMapView?.onSaveInstanceState(outState)
    }
}
