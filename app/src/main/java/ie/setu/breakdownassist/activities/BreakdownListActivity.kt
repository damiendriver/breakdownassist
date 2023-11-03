package ie.setu.breakdownassist.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import ie.setu.breakdownassist.R
import ie.setu.breakdownassist.adapters.BreakdownAdapter
import ie.setu.breakdownassist.adapters.BreakdownListener
import ie.setu.breakdownassist.databinding.ActivityBreakdownListBinding
import ie.setu.breakdownassist.main.MainApp
import ie.setu.breakdownassist.models.BreakdownModel


class BreakdownListActivity : AppCompatActivity(), BreakdownListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityBreakdownListBinding
    private var position: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBreakdownListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        // binding.recyclerView.adapter = BreakdownAdapter(app.breakdowns)
        binding.recyclerView.adapter = BreakdownAdapter(app.breakdowns.findAll(),this)
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, BreakdownActivity::class.java)
                getResult.launch(launcherIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                (binding.recyclerView.adapter)?.
                // notifyItemRangeChanged(0,app.breakdowns.size)
                notifyItemRangeChanged(0,app.breakdowns.findAll().size)
            }
        }

    override fun onBreakdownClick(breakdown: BreakdownModel, pos : Int) {
        val launcherIntent = Intent(this, BreakdownActivity::class.java)
        launcherIntent.putExtra("breakdown_edit", breakdown)
        position = pos
        getClickResult.launch(launcherIntent)
    }

    private val getClickResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                (binding.recyclerView.adapter)?.
                notifyItemRangeChanged(0,app.breakdowns.findAll().size)
            }
            else // Deleting
                if (it.resultCode == 99)     (binding.recyclerView.adapter)?.notifyItemRemoved(position)
        }
}