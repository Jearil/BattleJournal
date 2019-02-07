// Copyright (c) Colin Miller 2019.

package com.battlejournal.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.battlejournal.ArmyActivity
import com.battlejournal.R
import com.battlejournal.adapter.RecordAdapter
import com.battlejournal.controller.ArmyController
import com.battlejournal.ui.viewmodels.ArmyViewModel
import com.battlejournal.ui.viewmodels.RecordViewModel
import kotlinx.android.synthetic.main.army_edit_fragment.*

class ArmyEditFragment : Fragment() {

  companion object {
    fun newInstance() = ArmyEditFragment()
  }

  private lateinit var recordViewModel: RecordViewModel
  private lateinit var viewModel: ArmyViewModel
  private lateinit var controller: ArmyController

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
    controller = ArmyController(
      viewModel,
      this,
      allianceSpinner,
      factionSpinner2,
      armyNameEdit,
      delete,
      save,
      recordsRecyclerView,
      addGameRecordButton
    )

    /*
    recordsRecyclerView.adapter = adapter
    recordsRecyclerView.layoutManager = LinearLayoutManager(context)
    alliance.setText(R.string.alliance)

    viewModel.getFirestoreDocument().observe(this, Observer { dataSnapshot ->
      if (dataSnapshot == null) {
        return@Observer
      }
      val army = dataSnapshot.toObject(Army::class.java)?.withId<Army>(dataSnapshot.id)

      armyNameEdit.setText(army?.name)

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

    addGameRecordButton.setOnClickListener(
      Navigation.createNavigateOnClickListener(R.id.action_armyEditFragment_to_placeholder, null)
    )

    delete.setOnClickListener {
      //TODO: Add a modal to ask if they're sure
      val deleteString = getString(R.string.delete_success, armyNameEdit.text.toString())
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
    */
  }

}
