package ie.setu.breakdownassist.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import ie.setu.breakdownassist.R
import ie.setu.breakdownassist.databinding.ActivityBreakdownBinding
import ie.setu.breakdownassist.main.MainApp
import ie.setu.breakdownassist.models.BreakdownModel
import timber.log.Timber
import timber.log.Timber.Forest.i

class BreakdownActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBreakdownBinding
    var breakdown = BreakdownModel()
    lateinit var app : MainApp
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_breakdown)

        binding = ActivityBreakdownBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp
        i("Breakdown Assist has started..")

        binding.btnAdd.setOnClickListener() {
            breakdown.title = binding.breakdownTitle.text.toString()
            breakdown.description = binding.description.text.toString()
            if (breakdown.title.isNotEmpty()) {
                app.breakdowns.add(breakdown.copy())
                i("add Button Pressed: ${breakdown}")
                for (i in app.breakdowns.indices)
                { i("Breakdown[$i]:${this.app.breakdowns[i]}") }
            }
            else {
                Snackbar.make(it,"Please Enter a Report", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }
}