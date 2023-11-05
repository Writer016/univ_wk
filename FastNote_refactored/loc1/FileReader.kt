package com.appl.fastnote_ref.FileReader

import android.content.Context
import java.io.File

abstract class FileReader {
    fun sortWithDate(fileArr: Array<out File>?): Array<out File>{ //<< 가져온 리스트 정렬 LOGIC >>

        val sortedfileArr = if(fileArr!=null){ //리스트가 null이 아니면..
            val comparator: Comparator<File> = compareBy { it.lastModified() } //날짜순 정렬
            fileArr.sortedWith(comparator).reversed().toTypedArray()
        } else{ //리스트가 null이면..
            arrayOf()
        } //메모 리스트를 정렬 후 fileArr로

        return sortedfileArr //!!: 해당 변수는 non-null(null 값일 수 없는)이라는 의미.
    }

    fun readFiles(context: Context): Array<out File> {
        val fileArr = context.filesDir.listFiles { file ->
            file.name.endsWith(getMemoFileSuffix()) //파일명이 'n.txt'로 끝나는 파일들을 list 형태로 가져오기
        } //가져온 메모 리스트를 fileArr로 초기화
        return fileArr
    }

    abstract fun getMemoFileSuffix(): String
}