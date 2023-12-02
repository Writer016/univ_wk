package com.appl.fastnote_ref.Saver

abstract class Helper(private val saver: Saver) { //기능부 최상위 클래스
    fun saveNote(path: String, fileTitle: String, fileInfo: String, currentInfo: String){
        saver.saveNote(path, fileTitle, fileInfo, currentInfo)
    }
    fun ifNameEmpty(fileTitle: String): String{
        return saver.ifNameEmpty(fileTitle)
    }
}