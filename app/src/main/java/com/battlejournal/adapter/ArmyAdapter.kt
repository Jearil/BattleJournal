// Copyright (c) Colin Miller 2019.

package com.battlejournal.adapter

import android.support.v7.util.DiffUtil
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.battlejournal.R
import com.battlejournal.models.Army

class ArmyAdapter(var data : List<Army> = ArrayList()) : RecyclerView.Adapter<ArmyAdapter.ArmyViewHolder>() {

    class ArmyViewHolder(card : CardView) : RecyclerView.ViewHolder(card) {
        val name : TextView = card.findViewById(R.id.army_name)
        val winlosstie : TextView = card.findViewById(R.id.winlosstie)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArmyViewHolder {
        val armyCard = LayoutInflater.from(parent.context)
            .inflate(R.layout.army_card, parent, false) as CardView

        return ArmyViewHolder(armyCard)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ArmyViewHolder, position: Int) {
        holder.name.text = data[position].name
        holder.winlosstie.text = "W:0 L:0 T:0"
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.army_card
    }

    fun setArmies(newData : List<Army>) {
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