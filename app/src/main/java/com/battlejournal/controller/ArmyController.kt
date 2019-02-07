// Copyright (c) Colin Miller 2019.

package com.battlejournal.controller

import android.view.View
import android.widget.*
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.battlejournal.adapter.TextSpinnerAdapter
import com.battlejournal.database.ClassSnapshotModelParser
import com.battlejournal.models.Alliance
import com.battlejournal.models.Faction
import com.battlejournal.ui.viewmodels.ArmyViewModel
import com.firebase.ui.firestore.FirestoreArray
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore

class ArmyController(
  val viewModel: ArmyViewModel,
  val lifecycleOwner: LifecycleOwner,
  val allianceSpinner: Spinner,
  val factionSpinner: Spinner,
  val armyEdit: EditText,
  val deleteButton: Button,
  val saveButton: Button,
  val armyList: RecyclerView,
  val addRecord: FloatingActionButton
) {

  val allianceQuery = FirebaseFirestore.getInstance().collection("systems").document("aos")
    .collection("alliances")
  val snapShots = FirestoreArray(allianceQuery.orderBy("name"), ClassSnapshotModelParser(Alliance::class.java))
  val allianceAdapter = object : TextSpinnerAdapter<Alliance>(snapShots) {
    override fun populateView(view: View, model: Alliance, position: Int) {
      val textView = view as TextView
      textView.text = model.name
    }
  }

  var factionAdapter: TextSpinnerAdapter<Faction>? = null

  init {
    monitorView()
    setupAllianceSpinner()
    setupFactionSpinner()
  }

  private fun monitorView() {
    viewModel.getFirestoreDocument(lifecycleOwner).observe(lifecycleOwner, Observer { armySnapshot ->
      if (armySnapshot == null) {
        return@Observer
      }
    })
  }

  private fun setupAllianceSpinner() {
    lifecycleOwner.lifecycle.addObserver(allianceAdapter)
    allianceSpinner.adapter = allianceAdapter

    allianceSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
      override fun onNothingSelected(parent: AdapterView<*>?) {

      }

      override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val alliance = allianceAdapter.getItem(position)
        val factionSnapshot = FirestoreArray(
          allianceQuery.document(alliance.id)
            .collection("factions").orderBy("name"), ClassSnapshotModelParser(Faction::class.java)
        )
        val adapter = factionAdapter
        if (adapter == null) {
          factionAdapter = object : TextSpinnerAdapter<Faction>(factionSnapshot) {
            override fun populateView(view: View, model: Faction, position: Int) {
              val textView = view as TextView
              textView.text = model.name
            }
          }
          factionSpinner.adapter = factionAdapter
        } else {
          adapter.updateListener(factionSnapshot)
        }
      }

    }
  }

  private fun setupFactionSpinner() {

  }

}