package com.battlejournal.ui.recordsheet

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.battlejournal.R

class RecordSheetFragment : Fragment() {

    companion object {
        fun newInstance() = RecordSheetFragment()
    }

    private lateinit var viewModel: RecordSheetViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.record_sheet_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(RecordSheetViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
