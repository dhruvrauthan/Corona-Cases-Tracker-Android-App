package com.example.coronatracker.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.coronatracker.R
import com.example.coronatracker.activities.MainActivity
import com.example.coronatracker.activities.StateActivity

class SearchFragment : Fragment() {

    private lateinit var root: View

    //UI
    private lateinit var mStatesAdapter: ArrayAdapter<String>
    private lateinit var mStatesListView: ListView

    //Variables
    private val mStatesList = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        root = inflater.inflate(R.layout.fragment_search, container, false)

        (activity as MainActivity).supportActionBar?.title="Search"

        setHasOptionsMenu(true)

        initView()

        mStatesListView.setOnItemClickListener { parent, view, position, id ->

            val intent = Intent(activity, StateActivity::class.java)
            intent.putExtra("pos", position)
            activity?.startActivity(intent)

        }

        return root

    }

    private fun initView() {

        mStatesListView = root.findViewById(R.id.states_listview)

        mStatesList.addAll(
            listOf(
                "Andaman and Nicobar Islands", "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chandigarh",
                "Chhattisgarh", "Dadra and Nagar Haveli", "Daman and Diu", "Delhi", "Goa", "Gujarat", "Haryana", "Himachal Pradesh",
                "Jammu and Kashmir", "Jharkhand", "Karnataka", "Kerala", "Ladakh", "Lakshwadweep", "Madhya Pradesh", "Maharashtra",
                "Manipur", "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Puducherry", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu",
                "Telangana", "Tripura", "Uttar Pradesh", "Uttarakhand", "West Bengal"
                )
        )

        mStatesListView.isTextFilterEnabled = true

        mStatesAdapter = ArrayAdapter<String>(
            requireActivity().applicationContext,
            android.R.layout.simple_list_item_1,
            mStatesList
        )
        mStatesListView.adapter = mStatesAdapter

    }
}
