// Copyright (c) Colin Miller 2019.

package com.battlejournal.models

import java.util.*

data class Record(
  val opponent: String = "",
  val opponentFaction: String = "",
  val datePlayed: Date = Date(),
  val result: String = "",
  val notes: String = "",
  val gameStyle: String = "",
  val myPoints: Int = -1,
  val opponentsPoints: Int = -1
) : Model()