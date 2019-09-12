// Copyright (c) Colin Miller 2019.

package com.battlejournal.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.FrameLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import com.battlejournal.R
import com.battlejournal.adapter.TextSpinnerAdapter
import com.battlejournal.database.ClassSnapshotModelParser
import com.battlejournal.models.Alliance
import com.battlejournal.models.Faction
import com.firebase.ui.firestore.FirestoreArray
import com.google.firebase.firestore.FirebaseFirestore

/**
 * //TODO: Add documentation
 */
class AllianceFactionSlider : FrameLayout {

  var currentAlliance: Alliance? = null
  var currentFaction: Faction? = null
  var allianceSelector: AdapterView.OnItemSelectedListener? = null
  var factionSelector: AdapterView.OnItemSelectedListener? = null

  private lateinit var allianceSpinner: Spinner
  private lateinit var factionSpinner: Spinner

  private val allianceQuery = FirebaseFirestore.getInstance().collection("systems").document("aos")
    .collection("alliances")
  private val snapShots = FirestoreArray(allianceQuery.orderBy("name"), ClassSnapshotModelParser(Alliance::class.java))
  private val allianceAdapter = object : TextSpinnerAdapter<Alliance>(snapShots) {
    override fun populateView(view: View, model: Alliance, position: Int) {
      val textView = view as TextView
      textView.text = model.name
    }
  }

  private var factionAdapter: TextSpinnerAdapter<Faction>? = null

  constructor(context: Context) : super(context) {
    initializeView(context)
  }

  constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
    initializeView(context)
  }

  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
    context,
    attrs,
    defStyleAttr
  ) {
    initializeView(context)
  }

  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
    context,
    attrs,
    defStyleAttr,
    defStyleRes
  ) {
    initializeView(context)
  }

  private fun initializeView(context: Context) {
    val inflator = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    inflator.inflate(R.layout.alliance_faction_component, this)
  }

  override fun onFinishInflate() {
    super.onFinishInflate()

    allianceSpinner = findViewById(R.id.allianceSpinner)
    factionSpinner = findViewById(R.id.factionSpinner)
  }

  fun isFilledOut(): Boolean {
    return currentAlliance != null && currentFaction != null
  }

  fun setupAllianceSpinner(lifecycleOwner: LifecycleOwner) {
    lifecycleOwner.lifecycle.addObserver(allianceAdapter)
    allianceSpinner.adapter = allianceAdapter

    allianceSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
      override fun onNothingSelected(parent: AdapterView<*>?) {
        allianceSelector?.onNothingSelected(parent)
      }

      override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val alliance = allianceAdapter.getItem(position)
        currentAlliance = alliance
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
        allianceSelector?.onItemSelected(parent, view, position, id)
      }

    }

    factionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
      override fun onNothingSelected(parent: AdapterView<*>?) {
        factionSelector?.onNothingSelected(parent)
      }

      override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        currentFaction = factionAdapter?.getItem(position)
        factionSelector?.onItemSelected(parent, view, position, id)
      }
    }
  }
}