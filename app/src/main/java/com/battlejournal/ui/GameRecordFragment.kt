// Copyright (c) Colin Miller 2019.

package com.battlejournal.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.battlejournal.R
import com.battlejournal.databinding.GameRecordBinding
import com.battlejournal.ui.viewmodels.GameRecordViewModel
import kotlinx.android.synthetic.main.game_record.*
import java.util.*

class GameRecordFragment : Fragment() {

  companion object {
    fun newInstance() = GameRecordFragment()
  }

  private lateinit var viewModel: GameRecordViewModel
  private lateinit var binding: GameRecordBinding

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = DataBindingUtil.inflate(layoutInflater, R.layout.game_record, container, false)
    binding.fragment = this
    return binding.root
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProvider(this).get(GameRecordViewModel::class.java)
    binding.viewModel = viewModel
    allianceFactionSlider.setupAllianceSpinner(this)

    val now = Calendar.getInstance()
    val datePlayed = viewModel.datePlayed.get()
    if (datePlayed != null) {
      now.time = datePlayed
    }
    val month = now.get(Calendar.MONTH)
    val day = now.get(Calendar.DAY_OF_MONTH)
    val year = now.get(Calendar.YEAR)
    gameDateText.setText(getString(R.string.formatted_date, month + 1, day, year))
    gameDateText.setOnClickListener {
      val currentContext = context
      if (currentContext != null) {
        val dialog = DatePickerDialog(
          currentContext,
          DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            val date = Calendar.getInstance()
            date.set(year, month, dayOfMonth)
            viewModel.datePlayed.set(date.time)
          },
          year,
          month,
          day
        )
        dialog.show()
      }
    }
  }

  fun getSaveClickListener(): View.OnClickListener {
    return View.OnClickListener {
      val record = viewModel.updateRecord()
      if (record.myPoints < 0) {
        viewModel.notes.set("view model")
      }
    }
  }
}
