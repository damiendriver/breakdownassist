package ie.setu.breakdownassist.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import ie.setu.breakdownassist.R
import ie.setu.breakdownassist.databinding.ActivityBreakdownBinding
import ie.setu.breakdownassist.helpers.showImagePicker
import ie.setu.breakdownassist.main.MainApp
import ie.setu.breakdownassist.models.BreakdownModel
import ie.setu.breakdownassist.models.Location
import timber.log.Timber.Forest.i

class BreakdownActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBreakdownBinding
    var breakdown = BreakdownModel()
    lateinit var app: MainApp
    private lateinit var imageIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher: ActivityResultLauncher<Intent>
    var edit = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        edit = false
        setContentView(R.layout.activity_breakdown)

        binding = ActivityBreakdownBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)


        app = application as MainApp
        i("Breakdown Assist has started..")

        if (intent.hasExtra("breakdown_edit")) {
            edit = true
            breakdown = intent.extras?.getParcelable("breakdown_edit")!!
            binding.btnAdd.setText(R.string.save_breakdown)
            binding.breakdownTitle.setText(breakdown.title)
            binding.description.setText(breakdown.description)
            binding.phone.setText(breakdown.phone)
            Picasso.get()
                .load(breakdown.image)
                .into(binding.breakdownImage)
        if (breakdown.image != Uri.EMPTY) {
            binding.chooseImage.setText(R.string.change_breakdown_image)
        }
        }

        binding.btnAdd.setOnClickListener() {
            breakdown.title = binding.breakdownTitle.text.toString()
            breakdown.description = binding.description.text.toString()
            breakdown.phone = binding.phone.text.toString()
            if (breakdown.title.isEmpty()) {
                Snackbar.make(it, R.string.enter_breakdown_title, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                if (edit) {
                    app.breakdowns.update(breakdown.copy())
                } else {
                    app.breakdowns.create(breakdown.copy())
                }
            }
            i("add Button Pressed: $breakdown")
            setResult(RESULT_OK)
            finish()
        }

        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher,this)
        }

        binding.breakdownLocation.setOnClickListener {
            val location = Location(52.249733, -6.340115, 15f)
            if (breakdown.zoom != 0f) {
                location.lat = breakdown.lat
                location.lng = breakdown.lng
                location.zoom = breakdown.zoom
            }
            val launcherIntent = Intent(this, MapActivity::class.java)
                .putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
        }

        registerImagePickerCallback()
        registerMapCallback()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_breakdown, menu)
        if (edit) menu.getItem(0).isVisible = true
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_delete -> {
                setResult(99)
                app.breakdowns.delete(breakdown)
                setResult(RESULT_OK)
                finish()
            }
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")

                            val image = result.data!!.data!!
                            contentResolver.takePersistableUriPermission(image,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            breakdown.image = image

                            Picasso.get()
                                .load(breakdown.image)
                                .into(binding.breakdownImage)
                            binding.chooseImage.setText(R.string.change_breakdown_image)
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Location ${result.data.toString()}")
                            val location =
                                result.data!!.extras?.getParcelable<Location>("location")!!
                            i("Location == $location")
                            breakdown.lat = location.lat
                            breakdown.lng = location.lng
                            breakdown.zoom = location.zoom
                        } // end of if
                    }

                    RESULT_CANCELED -> {}
                    else -> {}
                }
            }
    }
}