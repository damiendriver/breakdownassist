package ie.setu.breakdownassist.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import ie.setu.breakdownassist.R
import ie.setu.breakdownassist.databinding.ActivityBreakdownBinding
import ie.setu.breakdownassist.main.MainApp
import ie.setu.breakdownassist.models.BreakdownModel
import timber.log.Timber.Forest.i

class BreakdownActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBreakdownBinding
    var breakdown = BreakdownModel()
    lateinit var app : MainApp
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var edit = false
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
        }

        binding.btnAdd.setOnClickListener() {
            breakdown.title = binding.breakdownTitle.text.toString()
            breakdown.description = binding.description.text.toString()
            breakdown.phone = binding.phone.text.toString()
            if (breakdown.title.isEmpty()) {
                Snackbar.make(it,R.string.enter_breakdown_title, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                if (edit) {
                    app.breakdowns.update(breakdown.copy())
                } else {
                    app.breakdowns.create(breakdown.copy())
                }
            }
            setResult(RESULT_OK)
            finish()
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_breakdown, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}