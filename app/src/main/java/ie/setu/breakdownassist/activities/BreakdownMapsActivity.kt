package ie.setu.breakdownassist.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ie.setu.breakdownassist.databinding.ActivityBreakdownMapsBinding

class BreakdownMapsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBreakdownMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBreakdownMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

    }
}