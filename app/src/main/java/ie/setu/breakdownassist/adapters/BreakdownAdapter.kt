package ie.setu.breakdownassist.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ie.setu.breakdownassist.databinding.CardBreakdownBinding
import ie.setu.breakdownassist.models.BreakdownModel

interface BreakdownListener {
    fun onBreakdownClick(breakdown: BreakdownModel)
}
class BreakdownAdapter constructor(private var breakdowns: List<BreakdownModel>,
                                   private val listener: BreakdownListener) :
    RecyclerView.Adapter<BreakdownAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardBreakdownBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val breakdown = breakdowns[holder.adapterPosition]
        holder.bind(breakdown, listener)
    }

    override fun getItemCount(): Int = breakdowns.size

    class MainHolder(private val binding : CardBreakdownBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(breakdown: BreakdownModel, listener: BreakdownListener) {
            binding.breakdownTitle.text = breakdown.title
            binding.description.text = breakdown.description
            binding.phone.text = breakdown.phone
            Picasso.get().load(breakdown.image).resize(200,200).into(binding.imageIcon)
            binding.root.setOnClickListener { listener.onBreakdownClick(breakdown) }
        }
    }
}