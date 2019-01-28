// Copyright (c) Colin Miller 2019.

package com.battlejournal.models

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
class Army(
  val name: String = "",
  val faction: String = ""
) : Model()