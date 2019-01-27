// Copyright (c) Colin Miller 2019.

package com.battlejournal

import android.os.Bundle

class ArmyActivity : BaseAuthActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.record_sheet_activity)
  }

}
