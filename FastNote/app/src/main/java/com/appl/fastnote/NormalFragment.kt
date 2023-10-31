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
import com.appl.fastnote.adapter.NormalAdapter
import java.io.File
import kotlin.Comparator

class NormalFragment : Fragment() { //일반 메모 열람 페이지

    private lateinit var mainActivity: MainActivity
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: NormalAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val inf = inflater.inflate(R.layout.fragment_normal, container, false) //view inflate

        //<< 메모 리스트 가져오기 LOGIC >>
        //mainActivity.filesDir: 본 어플리케이션에 대한 내부 스토리지 경로 반환
        //File.listfiles(Filefilter filter): Filefilter 객체를 인자로 받아 처리
        //FileFilter 인터페이스 구현 시, 람다'{}'에 구현한 accept() 메서드는 listfiles 메서드가 파일리스트를 불러올 때 파일 필터링 목적으로 사용한다.
        var fileArr = mainActivity.filesDir.listFiles { file ->
            file.name.endsWith("n.txt") //파일명이 'n.txt'로 끝나는 파일들을 list 형태로 가져오기
        } //가져온 메모 리스트를 fileArr로 초기화

        //<< 가져온 리스트 정렬 LOGIC >>
        fileArr = if(fileArr!=null){ //리스트가 null이 아니면..
            val comparator: Comparator<File> = compareBy { it.lastModified() } //날짜순 정렬
            fileArr.sortedWith(comparator).reversed().toTypedArray()
        //List.sortedWith(comparator): comparator 객체 초기화 시 지정한 정렬 방식을 리스트에 적용
        //toTypedArray(): List->Array 변환
        } else{ //리스트가 null이면..
            arrayOf() //널 값 방지 목적으로, 디폴트 값 설정.
        } //메모 리스트를 정렬 후 fileArr로

        //RecyclerView 초기 설정
        viewAdapter = NormalAdapter(fileArr.toCollection(ArrayList()), mainActivity)
        recyclerView = inf.findViewById(R.id.recyclerView_normal)
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