package com.appl.fastnote

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import java.io.File

abstract class BaseMemoFragment: Fragment() {
    lateinit var mainActivity: MainActivity
    lateinit var recyclerView: RecyclerView
    lateinit var inf: View
    lateinit var fileArr: Array<File>

    protected abstract fun getMemoFileSuffix(): String
    abstract fun getFragment(): Int
    abstract fun bindRecyclerView()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        inf = inflater.inflate(getFragment(), container, false) //view inflate

        fileArr = mainActivity.filesDir.listFiles { file ->
            file.name.endsWith(getMemoFileSuffix())
        }

        fileArr = if(fileArr!=null){
            val comparator: Comparator<File> = compareBy { it.lastModified() }
            fileArr.sortedWith(comparator).reversed().toTypedArray()
        } else{
            arrayOf()
        }

        bindRecyclerView()

        return inf
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }
}