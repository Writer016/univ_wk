package com.appl.fastnote_ref.Saver

class GeneralHelper(private val saver: Saver, private var fileTitle: String = "", private var fileInfo: String = "",
                    private var path: String = "", private var currentInfo: String = "") : Helper(saver) { //기능부 하위 클래스
    fun save(){
        saveNote(path, ifNameEmpty(fileTitle), fileInfo, currentInfo) //저장 시 클라이언트 코드에서 사용하게 되는 메서드
    }
}