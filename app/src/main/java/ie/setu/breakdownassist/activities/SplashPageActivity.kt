package ie.setu.breakdownassist.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import ie.setu.breakdownassist.R
import ie.setu.breakdownassist.databinding.ActivitySplashPageBinding
import timber.log.Timber.Forest.i

class SplashPageActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_splash_page)
        binding = ActivitySplashPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.carOwner.setOnClickListener() {
            i("Report Breakdown Button Pressed")
            val intent = Intent(this, CalloutServiceActivity::class.java)
            startActivity(intent)
        }

        binding.garageInfo.setOnClickListener() {
            i("Breakdown Garages Button Pressed")
            val intent = Intent(this, BreakdownListActivity::class.java)
            startActivity(intent)
        }
    }
}