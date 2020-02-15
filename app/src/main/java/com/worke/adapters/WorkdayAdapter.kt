package com.worke.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.worke.R
import com.worke.data.Workday

class WorkdayAdapter(var dataset: List<Workday?> = emptyList()):
    RecyclerView.Adapter<WorkdayAdapter.WorkdayViewHolder>() {

    fun setData(newData: List<Workday?>) {
        this.dataset = newData
        notifyDataSetChanged()
    }

    class WorkdayViewHolder(view: View): RecyclerView.ViewHolder(view) {

        var workdayTextView: TextView = view.findViewById(R.id.workday_tv)
        var dateTextView: TextView = view.findViewById(R.id.date_tv)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkdayViewHolder {
        val workdayViewHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.workday_view_holder_layout, parent, false)
        return WorkdayViewHolder(workdayViewHolder)
    }

    override fun getItemCount() = dataset.size

    override fun onBindViewHolder(holder: WorkdayViewHolder, position: Int) {
        holder.workdayTextView.text = dataset[position]?.displayTime
        holder.dateTextView.text = dataset[position]?.displayDate
    }

}