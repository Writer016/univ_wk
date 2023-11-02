package com.appl.fastnote_ref.Saver

interface Saver {
    fun generateName(fileTitle: String): String //<< 파일 이름 생성 LOGIC >> 선언
    fun saveFile(title: String, info: String) //<< 파일 저장 LOGIC >> 선언
}