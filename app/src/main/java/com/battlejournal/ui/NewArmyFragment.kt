// Copyright (c) Colin Miller 2019.

package com.battlejournal.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.battlejournal.ArmyActivity
import com.battlejournal.R
import com.battlejournal.adapter.AllianceSpinnerAdapter
import com.battlejournal.models.Army
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.new_army_fragment.*

class NewArmyFragment : Fragment() {

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.new_army_fragment, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    var factionValue: String? = null
    var itemSelected = false
    var allianceId: String? = null
    val allianceAdapater = AllianceSpinnerAdapter()
    allianceAdapater.startListening()
    allianceSpinner.adapter = allianceAdapater

    allianceSpinner.onItemSelectedListener = object : OnItemSelectedListener {
      override fun onNothingSelected(parent: AdapterView<*>?) {

      }

      override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val allianceView = view as TextView
        allianceId = allianceView.text.toString()
      }
    }

    factionSpinner.onItemSelectedListener = object : OnItemSelectedListener {
      override fun onNothingSelected(parent: AdapterView<*>?) {
        saveArmy.isEnabled = false
        itemSelected = false
      }

      override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        saveArmy.isEnabled = !armyName.text.isBlank()
        itemSelected = true
        val factionView = view as TextView
        factionValue = factionView.text.toString()
      }
    }

    armyName.addTextChangedListener(object : TextWatcher {
      override fun afterTextChanged(s: Editable?) {
        if (s.toString().isBlank()) {
          saveArmy.isEnabled = false
        } else if (itemSelected) {
          saveArmy.isEnabled = true
        }
      }

      override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

      override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })

    saveArmy.setOnClickListener {
      val db = FirebaseFirestore.getInstance()
      val parentActivity = activity as ArmyActivity
      val uid = parentActivity.uid
      val armies = db.collection("users").document(uid).collection("armies")
      val factionText = factionValue
      val allianceText = allianceId
      if (factionText == null) {
        return@setOnClickListener
      }
      if (allianceText == null) {
        return@setOnClickListener
      }
      val armyEntry = Army(armyName.text.toString(), allianceText, factionText)
      armies.add(armyEntry)
      parentActivity.onBackPressed()
    }
  }

  companion object {
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ArmyEditFragment.
     */
    @JvmStatic
    fun newInstance() =
      NewArmyFragment()
  }
}
