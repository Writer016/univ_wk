package com.appl.fastnote

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fastnote.R
import com.appl.fastnote.adapter.ProfileAdapter
import java.io.File

class ProfileFragment : Fragment() {

    private lateinit var mainActivity: MainActivity
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: ProfileAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val inf = inflater.inflate(R.layout.fragment_profile, container, false) //view inflate

        var fileArr: Array<out File>? = mainActivity.filesDir.listFiles {file->
            file.name.endsWith("p.txt")
        }

        fileArr = if(fileArr!=null){
            val comparator: Comparator<File> = compareBy { it.lastModified() }
            fileArr.sortedWith(comparator).reversed().toTypedArray()
        } else{
            arrayOf()
        }

        viewAdapter = ProfileAdapter(fileArr.toCollection(ArrayList()), mainActivity)
        recyclerView = inf.findViewById(R.id.recyclerView_profile)
        recyclerView.apply {
            adapter = viewAdapter
            layoutManager = LinearLayoutManager(mainActivity)
        }

        // Inflate the layout for this fragment
        return inf
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onResume() {
        super.onResume()
        viewAdapter.saveChanges()
    }

}