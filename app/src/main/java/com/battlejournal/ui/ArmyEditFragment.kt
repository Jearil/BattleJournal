// Copyright (c) Colin Miller 2019.

package com.battlejournal.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import com.battlejournal.ArmyActivity
import com.battlejournal.R
import com.battlejournal.models.Army
import com.google.firebase.firestore.FirebaseFirestore

class ArmyEditFragment : Fragment() {

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_army_edit, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val save = view.findViewById<Button>(R.id.save_army)
    val faction = view.findViewById<Spinner>(R.id.faction_spinner)
    val name = view.findViewById<EditText>(R.id.army_name)
    var factionValue: String? = null
    var itemSelected = false
    faction.onItemSelectedListener = object : OnItemSelectedListener {
      override fun onNothingSelected(parent: AdapterView<*>?) {
        save.isEnabled = false
        itemSelected = false
      }

      override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        save.isEnabled = !name.text.isBlank()
        itemSelected = true
        val factionView = view as TextView
        factionValue = factionView.text.toString()
      }
    }

    name.addTextChangedListener(object : TextWatcher {
      override fun afterTextChanged(s: Editable?) {
        if (s.toString().isBlank()) {
          save.isEnabled = false
        } else if (itemSelected) {
          save.isEnabled = true
        }
      }

      override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

      override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })

    save.setOnClickListener {
      val db = FirebaseFirestore.getInstance()
      val parentActivity = activity as ArmyActivity
      val uid = parentActivity.uid
      val armies = db.collection("users").document(uid).collection("armies")
      val factionText = factionValue
      if (factionText == null) {
        return@setOnClickListener
      }
      val armyEntry = Army(name.text.toString(), factionText)
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
      ArmyEditFragment()
  }
}
