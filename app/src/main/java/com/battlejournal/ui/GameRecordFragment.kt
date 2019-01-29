// Copyright (c) Colin Miller 2019.

package com.battlejournal.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.battlejournal.R
import com.google.android.material.textfield.TextInputEditText
import java.util.*

class GameRecordFragment : Fragment() {

  companion object {
    fun newInstance() = GameRecordFragment()
  }

  private lateinit var viewModel: GameRecordViewModel

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.game_record_fragment, container, false)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProviders.of(this).get(GameRecordViewModel::class.java)

    val gameDate = view!!.findViewById<TextInputEditText>(R.id.gameDateText)
    val now = Calendar.getInstance()
    val month = now.get(Calendar.MONTH)
    val day = now.get(Calendar.DAY_OF_MONTH)
    val year = now.get(Calendar.YEAR)
    gameDate.setText(getString(R.string.formatted_date, month + 1, day, year))
    gameDate.setOnClickListener {
      val dialog = DatePickerDialog(
        context,
        DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
          gameDate.setText(getString(R.string.formatted_date, month + 1, dayOfMonth, year))
        },
        year,
        month,
        day
      )
      dialog.show()
    }
  }

}
