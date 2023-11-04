package com.appl.fastnote

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fastnote.R
import com.appl.fastnote.adapter.NormalAdapter

class NormalFragment: BaseMemoFragment() {
    private lateinit var viewAdapter: NormalAdapter

    override fun getMemoFileSuffix(): String {
        return "n.txt"
    }

    override fun getFragment(): Int {
        return R.layout.fragment_normal
    }

    override fun bindRecyclerView(){
        viewAdapter = NormalAdapter(fileArr.toCollection(ArrayList()), mainActivity)
        recyclerView = inf.findViewById(R.id.recyclerView_normal)
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