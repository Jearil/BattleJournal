// Copyright (c) Colin Miller 2019.

package com.battlejournal.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.battlejournal.ArmyActivity
import com.battlejournal.R
import com.battlejournal.adapter.ArmyAdapter
import com.battlejournal.models.AllArmyViewModel
import com.battlejournal.models.Army

class ArmyFragment : Fragment() {

  companion object {
    fun newInstance() = ArmyFragment()
  }

  private lateinit var viewModel: AllArmyViewModel
  private var adapter = ArmyAdapter()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.army_fragment, container, false)
    val recyclerView = view.findViewById<RecyclerView>(R.id.armyRecyclerView)
    val addArmyButton = view.findViewById<FloatingActionButton>(R.id.addArmyButton)
    recyclerView.adapter = adapter
    recyclerView.layoutManager = LinearLayoutManager(context)

    addArmyButton.setOnClickListener(
      Navigation.createNavigateOnClickListener(
        R.id.action_recordSheetFragment_to_armyEditFragment,
        null
      )
    )
    return view
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    val parentActivity = activity as ArmyActivity
    val uid = parentActivity.uid
    val factory = AllArmyViewModel.AllArmyViewModelFactory(uid)
    viewModel = ViewModelProviders.of(this, factory).get(AllArmyViewModel::class.java)
    viewModel.getFirestoreSnapshot().observe(this, Observer { dataSnapshot ->
      val armies = ArrayList<Army>()
      dataSnapshot?.documents?.forEach { doc ->
        val army = doc.toObject(Army::class.java)
        army?.let {armies.add(army)}
      }
      adapter.setArmies(armies)
    })
  }

}