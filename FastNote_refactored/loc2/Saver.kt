package com.appl.fastnote_ref.Saver

interface Saver { //구현부 최상위 클래스
    fun generateFileName(fileTitle: String): String //<< 파일 이름 생성 LOGIC >>
    fun saveFile(title: String, info: String) //<< 파일 저장 LOGIC >>
    fun ifNameEmpty(fileTitle: String): String{ //타이틀이 공백인지 체크 (WriteActivity의 saveAndFinish()의 일부)
        return if(fileTitle.trim()=="") "newNote"
        else fileTitle
    }
    fun saveNote(path: String, fileTitle: String, fileInfo: String, currentInfo: String) //메모 수정여부 체크 (WriteActivity의 saveAndFinish()의 일부)
}