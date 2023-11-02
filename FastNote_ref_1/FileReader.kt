package com.appl.fastnote_ref.FileReader

import java.io.File

abstract class FileReader {
    abstract fun readFiles(): Array<out File> //<< 메모 리스트 가져오기 LOGIC >> 선언
    fun sortWithDate(fileArr: Array<out File>?): Array<out File>{ //<< 가져온 리스트 정렬 LOGIC >> 구현
        //?: 해당 변수는 nullable(null 값일 수 있는)이라는 의미.
        //fileArr 정렬 로직 구현
        return fileArr!! //!!: 해당 변수는 non-null(null 값일 수 없는)이라는 의미.
    }
}