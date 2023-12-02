package com.appl.fastnote_ref.Saver

import android.content.Context
import android.widget.Toast
import com.example.fastnote.R
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter

class ProfileSaver(private val context: Context): Saver { //구현부 하위 클래스
    override fun saveNote(path: String, fileTitle: String, fileInfo: String, currentInfo: String){
        if (path == "null") {
            saveFile(generateFileName(fileTitle), fileInfo)
        } else {
            if (path.compareTo("${fileTitle}p.txt") != 0) {
                saveFile(generateFileName(fileTitle), fileInfo)
                File(context.filesDir, path).delete()
            } else {
                if (currentInfo.compareTo(fileInfo) != 0) {
                    saveFile(fileTitle, fileInfo)
                }
            }
        }
    }

    override fun saveFile(title: String, info: String) {
        try {
            val bufferedWriter = BufferedWriter(FileWriter("${context.filesDir}/${title}p.txt"))
            bufferedWriter.write(info)
            bufferedWriter.close()
        } catch (e: Exception) {
            context.apply {
                Toast.makeText(
                    this,
                    R.string.warn,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun generateFileName(title: String): String {
        var newTitle = title
        var index = 1
        if(File(context.filesDir, "${title}p.txt").exists()) {
            while(true){
                if(File(context.filesDir, "${title}-${index}p.txt").exists()){
                    index++
                }else{
                    newTitle = "${title}-${index}"
                    break
                }
            }
        }
        return newTitle
    }
}