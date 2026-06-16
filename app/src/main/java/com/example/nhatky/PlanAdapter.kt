package com.example.nhatky

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PlanAdapter(
    private var plans: List<PlanEntry>,
    private val onUpdate: (PlanEntry) -> Unit,
    private val onDelete: (PlanEntry) -> Unit
) : RecyclerView.Adapter<PlanAdapter.PlanViewHolder>() {

    class PlanViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.txtPlanTitle)
        val desc: TextView = view.findViewById(R.id.txtPlanDesc)
        val checkbox: CheckBox = view.findViewById(R.id.cbPlan)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDeletePlan)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_plan, parent, false)
        return PlanViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlanViewHolder, position: Int) {
        val plan = plans[position]
        holder.title.text = plan.title
        holder.desc.text = plan.description
        holder.checkbox.isChecked = plan.isCompleted

        holder.checkbox.setOnCheckedChangeListener { _, isChecked ->
            onUpdate(plan.copy(isCompleted = isChecked))
        }

        holder.btnDelete.setOnClickListener {
            onDelete(plan)
        }
    }

    override fun getItemCount() = plans.size

    fun updateData(newPlans: List<PlanEntry>) {
        plans = newPlans
        notifyDataSetChanged()
    }
}
