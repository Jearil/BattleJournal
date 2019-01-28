package com.battlejournal.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.battlejournal.ArmyActivity

import com.battlejournal.R
import com.battlejournal.models.Army
import com.crashlytics.android.Crashlytics

class ArmyEditFragment : Fragment() {

  companion object {
    fun newInstance() = ArmyEditFragment()
  }

  private lateinit var viewModel: ArmyEditViewModel
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
    alliance.setText(R.string.alliance)

    viewModel.getFirestoreDocument().observe(this, Observer { dataSnapshot ->
      if (dataSnapshot == null) {
        return@Observer
      }
      val army = dataSnapshot.toObject(Army::class.java)

      armyName.text = army?.name
      faction.text = army?.faction
    })

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
