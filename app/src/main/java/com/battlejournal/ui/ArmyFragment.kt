// Copyright (c) Colin Miller 2019.

package com.battlejournal.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.battlejournal.ArmyActivity
import com.battlejournal.R
import com.battlejournal.adapter.ArmyAdapter
import com.battlejournal.models.AllArmyViewModel
import com.battlejournal.models.Army
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ArmyFragment : Fragment() {

  companion object {
    fun newInstance() = ArmyFragment()
  }

  private lateinit var viewModel: AllArmyViewModel
  private var adapter = ArmyAdapter(object : ArmyAdapter.ArmyItemClicked {
    override fun onItemClicked(army: Army) {
      val action = ArmyFragmentDirections.actionArmyFragmentToArmyEditFragment(army.id)
      findNavController().navigate(action)
    }
  })

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
        R.id.action_armyFragment_to_newArmyFragment,
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
        val army = doc.toObject(Army::class.java)?.withId<Army>(doc.id)
        army?.let { armies.add(army) }
      }
      adapter.setArmies(armies)
    })
  }

}
