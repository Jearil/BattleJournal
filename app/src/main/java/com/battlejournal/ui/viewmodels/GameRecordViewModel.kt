// Copyright (c) Colin Miller 2019.

package com.battlejournal.ui.viewmodels

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.battlejournal.models.Record
import java.util.*

class GameRecordViewModel(
  var record: Record = Record(),
  val opponentEdit: ObservableField<String> = ObservableField(record.opponent),
  val opponentFaction: ObservableField<String> = ObservableField(record.opponentFaction),
  val datePlayed: ObservableField<Date> = ObservableField(record.datePlayed),
  val result: ObservableField<String> = ObservableField(record.result),
  val gameStyle: ObservableField<String> = ObservableField(record.gameStyle),
  val myPoints: ObservableField<Int> = ObservableField(record.myPoints),
  val opponentPoints: ObservableField<Int> = ObservableField(record.opponentsPoints),
  val notes: ObservableField<String> = ObservableField(record.notes)
) : ViewModel() {

  fun updateRecord(record: Record) {
    this.record = record
    opponentEdit.set(record.opponent)
    opponentFaction.set(record.opponentFaction)
    datePlayed.set(record.datePlayed)
    result.set(record.result)
    notes.set(record.notes)
    gameStyle.set(record.gameStyle)
    myPoints.set(record.myPoints)
    opponentPoints.set(record.opponentsPoints)
  }

  fun updateRecord(): Record {
    this.record = Record(
      opponentEdit.get()!!,
      opponentFaction.get()!!,
      datePlayed.get()!!,
      result.get()!!,
      notes.get()!!,
      gameStyle.get()!!,
      myPoints.get()!!,
      opponentPoints.get()!!
    )

    return record
  }
}
