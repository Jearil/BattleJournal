// Copyright (c) Colin Miller 2019.

package com.battlejournal.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.battlejournal.ArmyActivity
import com.battlejournal.R
import com.battlejournal.adapter.RecordAdapter
import com.battlejournal.models.Army
import com.battlejournal.models.Record
import com.battlejournal.models.RecordViewModel
import com.crashlytics.android.Crashlytics
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ArmyEditFragment : Fragment() {

  companion object {
    fun newInstance() = ArmyEditFragment()
  }

  private lateinit var recordViewModel: RecordViewModel
  private lateinit var viewModel: ArmyEditViewModel

  private var adapter = RecordAdapter()

  val args: ArmyEditFragmentArgs by navArgs()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.army_edit_fragment, container, false)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    val parentActivity = activity as ArmyActivity
    val uid = parentActivity.uid
    val factory = ArmyEditViewModel.ArmyEditViewModelFactory(uid, args.armyId)
    viewModel = ViewModelProviders.of(this, factory).get(ArmyEditViewModel::class.java)

    val armyName = view!!.findViewById<TextView>(R.id.armyName)
    val alliance = view!!.findViewById<TextView>(R.id.alliance)
    val faction = view!!.findViewById<TextView>(R.id.faction)
    val delete = view!!.findViewById<Button>(R.id.delete)
    val addGameButton = view!!.findViewById<FloatingActionButton>(R.id.addGameRecordButton)
    val recordRecycler = view!!.findViewById<RecyclerView>(R.id.recordsRecyclerView)
    recordRecycler.adapter = adapter
    recordRecycler.layoutManager = LinearLayoutManager(context)
    alliance.setText(R.string.alliance)

    viewModel.getFirestoreDocument().observe(this, Observer { dataSnapshot ->
      if (dataSnapshot == null) {
        return@Observer
      }
      val army = dataSnapshot.toObject(Army::class.java)?.withId<Army>(dataSnapshot.id)

      armyName.text = army?.name
      faction.text = army?.faction

      army?.let {
        val factory = RecordViewModel.AllArmyViewModelFactory(uid, army)
        recordViewModel = ViewModelProviders.of(this, factory).get(RecordViewModel::class.java)
        recordViewModel.getFirestoreSnapshot().observe(this, Observer { recordSnapshot ->
          val records = ArrayList<Record>()
          recordSnapshot?.documents?.forEach { doc ->
            val record = doc.toObject(Record::class.java)?.withId<Record>(doc.id)
            record?.let { records.add(record) }
          }
          adapter.setRecords(records)
        })
      }
    })

    addGameButton.setOnClickListener(
      Navigation.createNavigateOnClickListener(R.id.action_armyEditFragment_to_placeholder, null)
    )

    delete.setOnClickListener {
      //TODO: Add a modal to ask if they're sure
      val deleteString = getString(R.string.delete_success, armyName.text.toString())
      viewModel.delete()
        .addOnSuccessListener {
          Toast.makeText(context, deleteString, Toast.LENGTH_SHORT).show()
          parentActivity.onBackPressed()
        }
        .addOnFailureListener { ex ->
          Toast.makeText(context, R.string.delete_failure, Toast.LENGTH_SHORT).show()
          Crashlytics.logException(ex)
        }
    }
  }

}
