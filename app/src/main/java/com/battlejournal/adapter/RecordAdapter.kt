// Copyright (c) Colin Miller 2019.

package com.battlejournal.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.battlejournal.R
import com.battlejournal.models.Record
import java.text.SimpleDateFormat
import java.util.*

class RecordAdapter(var data: List<Record> = ArrayList()) : RecyclerView.Adapter<RecordAdapter.RecordViewHolder>() {

  private val format = SimpleDateFormat("MM/dd/yy", Locale.US)

  class RecordViewHolder(card: androidx.cardview.widget.CardView) : RecyclerView.ViewHolder(card) {
    val opponent: TextView = card.findViewById(R.id.opponent)
    val faction: TextView = card.findViewById(R.id.faction)
    val date: TextView = card.findViewById(R.id.date)
    val result: TextView = card.findViewById(R.id.result)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
    val record = LayoutInflater.from(parent.context)
      .inflate(R.layout.record_card, parent, false) as androidx.cardview.widget.CardView

    return RecordViewHolder(record)
  }

  override fun getItemCount(): Int {
    return data.size
  }

  override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
    val record = data[position]
    holder.opponent.text = record.opponent
    holder.faction.text = record.opponentFaction
    holder.result.text = record.result
    holder.date.text = format.format(record.datePlayed)
  }

  fun setRecords(newData: List<Record>) {
    val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
      override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return data[oldItemPosition] === newData[newItemPosition]
      }

      override fun getOldListSize(): Int {
        return data.size
      }

      override fun getNewListSize(): Int {
        return newData.size
      }

      override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return data[oldItemPosition] == newData[newItemPosition]
      }
    })

    data = newData
    diff.dispatchUpdatesTo(this)
  }

}