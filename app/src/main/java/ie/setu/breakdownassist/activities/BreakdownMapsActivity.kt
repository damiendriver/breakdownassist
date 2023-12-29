package ie.setu.breakdownassist.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.Picasso
import ie.setu.breakdownassist.databinding.ActivityBreakdownMapsBinding
import ie.setu.breakdownassist.databinding.ContentBreakdownMapsBinding
import ie.setu.breakdownassist.main.MainApp

class BreakdownMapsActivity : AppCompatActivity(), GoogleMap.OnMarkerClickListener {

    private lateinit var binding: ActivityBreakdownMapsBinding
    private lateinit var contentBinding: ContentBreakdownMapsBinding
    lateinit var map: GoogleMap
    lateinit var app: MainApp


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = application as MainApp

        binding = ActivityBreakdownMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        contentBinding = ContentBreakdownMapsBinding.bind(binding.root)
        contentBinding.mapView.onCreate(savedInstanceState)

        contentBinding.mapView.getMapAsync {
            map = it
            configureMap()
        }
    }
    private fun configureMap() {
        map.uiSettings.isZoomControlsEnabled = true
        app.breakdowns.findAll().forEach {
            val loc = LatLng(it.lat, it.lng)
            val options = MarkerOptions().title(it.title).position(loc)
            map.addMarker(options)?.tag = it.id
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.zoom))
        }
        map.setOnMarkerClickListener(this)
    }
    override fun onMarkerClick(marker: Marker): Boolean {
        //val breakdown = marker.tag as BreakdownModel
        val tag = marker.tag as Long
        val breakdown = app.breakdowns.findById(tag)
        contentBinding.currentTitle.text = breakdown!!.title
        contentBinding.currentDescription.text = breakdown.description
        contentBinding.currentPhone.text = breakdown.phone
        Picasso.get().load(breakdown.image).into(contentBinding.currentImage)
        return false
    }
    override fun onDestroy() {
        super.onDestroy()
        contentBinding.mapView.onDestroy()
    }
    override fun onLowMemory() {
        super.onLowMemory()
        contentBinding.mapView.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        contentBinding.mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        contentBinding.mapView.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        contentBinding.mapView.onSaveInstanceState(outState)
    }
}