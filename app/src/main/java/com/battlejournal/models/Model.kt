package com.battlejournal.models

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
open class Model {
  @Exclude
  lateinit var id: String

  open fun <T : Model?> withId(id: String): T {
    this.id = id
    return this as T
  }
}