package com.beratyesbek.vhoops.views.activities


import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.beratyesbek.vhoops.core.constants.Constants
import com.beratyesbek.vhoops.mvvm.MapsViewModel
import com.beratyesbek.vhoops.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener
    private lateinit var userLocation: LatLng
    private var receiverId: String? = null
    private var groupId: String? = null
    private var type: Int? = null

    private val viewModel : MapsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        checkGpsProvider()

        receiverId = intent.getStringExtra("receiverId")
        groupId = intent.getStringExtra("groupId")
        type = intent.getIntExtra("type", 0)

        viewModel.liveResult.observe(this,{
            if (it.success()){
                onBackPressed()
                Toast.makeText(this,"location was sent successfully",Toast.LENGTH_LONG)
            }else{
                Toast.makeText(this,"location wasn't sent",Toast.LENGTH_LONG)

            }
        })
    }

    fun shareLocation(view: View) {
        val senderId = FirebaseAuth.getInstance().currentUser.uid
        when(type){
            Constants.CHAT_ACTIVITY -> viewModel.shareLocationWithChat(senderId,receiverId!!,userLocation)
            Constants.GROUP_CHAT_ACTIVITY -> viewModel.shareLocationWithGroupChat(senderId,groupId!!,userLocation)
        }

    }
/*
    private fun shareLocationWithChat(senderId: String) {
        if (receiverId != null) {
            chatManager = ChatManager(ChatDal(),UserManager(UserDal()))
            chatManager.add(
                Chat(
                    senderId,
                    receiverId!!,
                    userLocation,
                    false,
                    Timestamp.now()
                )
            ) { result ->
                if (result.success()) {
                    onBackPressed()
                    finish()
                }
            }
        }
    }

    private fun shareLocationWithGroupChat(senderId: String) {
        if (groupId != null) {
            groupChatManager.add(
                GroupChat(
                    senderId,
                    groupId!!,
                    userLocation,
                    false,
                    Timestamp.now()
                )
            ) { result ->
                if (result.success()) {
                    onBackPressed()
                    finish()
                }
            }
        }

    }
*/
    fun checkGpsProvider() {
        val service = getSystemService(LOCATION_SERVICE) as LocationManager
        val enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if (!enabled) {

            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle("Warning")
            alertDialog.setMessage("You should turn on location service")
            alertDialog.setCancelable(false)
            alertDialog.setPositiveButton("Open") { dialog, which ->
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
                dialog.dismiss()
            }
            alertDialog.setNegativeButton("Close") { dialog, which ->

                dialog.dismiss()
            }

            alertDialog.show()
        } else {

            val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this)

        }
    }

    override fun onResume() {
        checkGpsProvider()
        super.onResume()
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.clear()
        mMap.setOnMapLongClickListener(clickListener)


        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = object : LocationListener {

            override fun onLocationChanged(location: Location) {
                if (location != null) {
                    mMap.clear()
                    userLocation = LatLng(location.latitude, location.longitude)
                    mMap.addMarker(MarkerOptions().position(userLocation).title("Your Location"))
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15f))

                    val geocoder = Geocoder(this@MapsActivity, Locale.getDefault())

                    try {
                        val addressList =
                            geocoder.getFromLocation(location.latitude, location.longitude, 1)

                        if (addressList != null && addressList.size > 0) {

                        }
                    } catch (e: Exception) {

                    }

                }
            }

        }

        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
        } else {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                5,
                10f,
                locationListener
            )
            val lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (lastLocation != null) {
                userLocation = LatLng(lastLocation.latitude, lastLocation.longitude)
                mMap.addMarker(MarkerOptions().position(userLocation).title("Your last Location"))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15f))
            }

        }
    }

    val clickListener = object : GoogleMap.OnMapLongClickListener {

        override fun onMapLongClick(p0: LatLng?) {
            mMap.clear()

            val geocoder = Geocoder(this@MapsActivity, Locale.getDefault())

            if (p0 != null) {
                var address = ""

                try {
                    val addressList = geocoder.getFromLocation(p0.latitude, p0.longitude, 1)

                    if (addressList != null && addressList.size > 0) {
                        if (addressList[0].thoroughfare != null) {
                            address += addressList.get(0).thoroughfare

                            if (addressList.get(0).subThoroughfare != null) {
                                address += addressList.get(0).subThoroughfare
                            }

                        }
                    }
                } catch (e: Exception) {

                }
                userLocation = p0
                mMap.addMarker(MarkerOptions().position(p0).title(address))
            } else {
                Toast.makeText(applicationContext, "Location Failed", Toast.LENGTH_LONG)
                    .show()
            }

        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1) {
            if (grantResults.size > 0) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        5,
                        10f,
                        locationListener
                    )
                }

            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
      