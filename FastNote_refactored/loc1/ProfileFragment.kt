package com.appl.fastnote

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fastnote.R
import com.appl.fastnote.adapter.ProfileAdapter

class ProfileFragment : BaseMemoFragment() {
    private lateinit var viewAdapter: ProfileAdapter

    override fun getMemoFileSuffix(): String {
        return "p.txt"
    }

    override fun getFragment(): Int {
        return R.layout.fragment_profile
    }

    override fun bindRecyclerView(){
        viewAdapter = ProfileAdapter(fileArr.toCollection(ArrayList()), mainActivity)
        recyclerView = inf.findViewById(R.id.recyclerView_profile)
        recyclerView.apply {
            adapter = viewAdapter
            layoutManager = LinearLayoutManager(mainActivity)
        }
    }

    override fun onResume() {
        super.onResume()
        viewAdapter.saveChanges()
    }
}