// Copyright (c) Colin Miller 2019.

package com.battlejournal.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.battlejournal.ArmyActivity
import com.battlejournal.R
import com.battlejournal.adapter.RecordAdapter
import com.battlejournal.models.Record
import com.battlejournal.ui.viewmodels.ArmyViewModel
import com.battlejournal.ui.viewmodels.RecordViewModel
import com.crashlytics.android.Crashlytics
import kotlinx.android.synthetic.main.army_edit_fragment.*

class ArmyEditFragment : Fragment() {

  companion object {
    fun newInstance() = ArmyEditFragment()
  }

  private lateinit var recordViewModel: RecordViewModel
  private lateinit var viewModel: ArmyViewModel

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
    val factory = ArmyViewModel.ArmyEditViewModelFactory(uid)
    viewModel = ViewModelProviders.of(this, factory).get(ArmyViewModel::class.java)
    viewModel.start(args.armyId)

    recordsRecyclerView.adapter = adapter
    recordsRecyclerView.layoutManager = LinearLayoutManager(context)
    alliance.setText(R.string.alliance)

    viewModel.getFirestoreDocument(this).observe(this, Observer { dataSnapshot ->
      if (dataSnapshot == null) {
        return@Observer
      }
      val army = dataSnapshot

      armyName.text = army.name
      alliance.text = army.allianceId
      faction.text = army.factionId

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
    })

    addGameRecordButton.setOnClickListener(
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
