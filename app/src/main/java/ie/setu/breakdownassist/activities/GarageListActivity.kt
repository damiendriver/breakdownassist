package ie.setu.breakdownassist.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ie.setu.breakdownassist.R
import ie.setu.breakdownassist.databinding.ActivityGarageListBinding
import ie.setu.breakdownassist.databinding.CardGarageBinding
import ie.setu.breakdownassist.main.MainApp
import ie.setu.breakdownassist.models.BreakdownModel

class GarageListActivity : AppCompatActivity() {

    lateinit var app: MainApp
    private lateinit var binding: ActivityGarageListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGarageListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = BreakdownAdapter(app.breakdowns)
    }
}

class BreakdownAdapter constructor(private var breakdowns: List<BreakdownModel>) :
    RecyclerView.Adapter<BreakdownAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardGarageBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val breakdown = breakdowns[holder.adapterPosition]
        holder.bind(breakdown)
    }

    override fun getItemCount(): Int = breakdowns.size

    class MainHolder(private val binding : CardGarageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(breakdown: BreakdownModel) {
            binding.garageTitle.text = breakdown.title
            binding.description.text = breakdown.description
        }
    }
}