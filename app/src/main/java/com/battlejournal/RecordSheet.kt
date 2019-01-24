package com.battlejournal

import android.os.Bundle
import com.battlejournal.ui.recordsheet.RecordSheetFragment

class RecordSheet : BaseAuthActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.record_sheet_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, RecordSheetFragment.newInstance())
                .commitNow()
        }
    }

}
