// Copyright (c) Colin Miller 2019.

package com.battlejournal.util

import androidx.databinding.BindingConversion
import java.text.SimpleDateFormat
import java.util.*

object DateConverter {

  @BindingConversion
  @JvmStatic
  fun displayDate(date: Date): String {
    return SimpleDateFormat("MM/dd/yyyy", Locale.US).format(date)
  }
}