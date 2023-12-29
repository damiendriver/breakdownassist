package ie.setu.breakdownassist.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ie.setu.breakdownassist.databinding.CardBreakdownBinding
import ie.setu.breakdownassist.helpers.customTransformation
import ie.setu.breakdownassist.models.BreakdownModel

interface BreakdownListener {
    fun onBreakdownClick(breakdown: BreakdownModel)
}

class BreakdownAdapter constructor(private var breakdowns: ArrayList<BreakdownModel>,
                                   private val listener: BreakdownListener,
                                   private val readOnly: Boolean) :
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

    fun removeAt(position: Int) {
        breakdowns.removeAt(position)
        notifyItemRemoved(position)
    }

    class MainHolder(private val binding : CardBreakdownBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(breakdown: BreakdownModel, listener: BreakdownListener) {
            binding.root.tag = breakdown
            binding.breakdown = breakdown
            binding.breakdownTitle.text = breakdown.title
            binding.description.text = breakdown.description
            binding.phone.text = breakdown.phone
            Picasso.get().load(breakdown.profilepic.toUri())
                .resize(200, 200)
                .transform(customTransformation())
                .centerCrop()
                .into(binding.imageIcon)
            binding.root.setOnClickListener { listener.onBreakdownClick(breakdown) }
            binding.executePendingBindings()
        }
    }
}