package com.battlejournal.ui.recordsheet

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.battlejournal.R
import com.battlejournal.RecordSheet
import com.battlejournal.adapter.ArmyAdapter
import com.battlejournal.models.AllArmyViewModel
import com.battlejournal.models.Army

class ArmyFragment : Fragment() {

    companion object {
        fun newInstance() = ArmyFragment()
    }

    private lateinit var viewModel: AllArmyViewModel
    private var adapter = ArmyAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.army_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.armyRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val parentActivity = activity as RecordSheet
        val uid = parentActivity.uid
        val factory = AllArmyViewModel.AllArmyViewModelFactory(uid)
        viewModel = ViewModelProviders.of(this, factory).get(AllArmyViewModel::class.java)
        viewModel.getFirestoreSnapshot().observe(this, Observer { dataSnapshot ->
            val armies = ArrayList<Army>()
            dataSnapshot?.documents?.forEach { doc ->
                val army = doc.toObject(Army::class.java)
                army?.let {armies.add(army)}
            }
            adapter.setArmies(armies)
        })
    }

}
