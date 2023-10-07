package com.appl.fastnote

import android.os.Bundle
import android.view.KeyEvent
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.example.fastnote.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileReader
import java.io.FileWriter

class WriteActivityNormal : AppCompatActivity() {
    private lateinit var fileTitleView: EditText
    private lateinit var fileInfoView: EditText
    private var currentFileInfo: String = ""
    private lateinit var pathData: String
    private var pressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_normal)

        fileTitleView = findViewById(R.id.normal_title)
        fileInfoView = findViewById(R.id.normal_info)
        val toolbar = findViewById<Toolbar>(R.id.Toolbar_write_normal)
        setSupportActionBar(toolbar)

        //set actionbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_save)

        pathData = intent.getStringExtra("pathData").toString()
        if(pathData!="null"){
            try{
                val bufferedReader = BufferedReader(FileReader("${filesDir}/${pathData}"))
                var str: String?
                val builder = StringBuilder()

                while(true){
                    str=bufferedReader.readLine()
                    if(str==null){break}
                    builder.append(str).append("\n")
                }
                bufferedReader.close()

                currentFileInfo = builder.toString()
                fileTitleView.setText("${pathData.subSequence(0, pathData.length-5)}")
                fileInfoView.setText(currentFileInfo)
           }
            catch(e: Exception){
                Toast.makeText(
                    this,
                    R.string.readWarn,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        val currentTime = System.currentTimeMillis()

        if(keyCode==KeyEvent.KEYCODE_BACK){
            if(currentTime-pressedTime <= 2000){
                finish()
            }else {
                pressedTime = System.currentTimeMillis()
                Toast.makeText(
                    this, R.string.saveWarn,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        return true
    }

    private fun saveFile(fileTitle: String){
        try {
            val bufferedWriter = BufferedWriter(FileWriter("${filesDir}/${fileTitle}n.txt"))
            bufferedWriter.write(fileInfoView.text.toString())
            bufferedWriter.close()
        } catch (e: Exception) {
            Toast.makeText(
                this,
                R.string.warn,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun generateName(title: String): String{
        var newTitle = title
        var index = 1
        if(File(filesDir, "${title}n.txt").exists()) {
            while(true){
                  if(File(filesDir, "${title}-${index}n.txt").exists()){
                            index++
                        }else{
                            newTitle = "${title}-${index}"
                            break
                        }
                    }
                }
        return newTitle
    }

    private fun saveAndFinish(){
        lifecycleScope.launch {
            if(fileTitleView.text.toString().trim()=="") fileTitleView.setText(R.string.newNote)
            if (pathData == "null") {
                withContext(Dispatchers.IO){
                    saveFile(generateName(fileTitleView.text.toString()))
                }
            } else {
                if (pathData.compareTo("${fileTitleView.text}n.txt") != 0) {
                    withContext(Dispatchers.IO) {
                        saveFile(generateName(fileTitleView.text.toString()))
                        File(filesDir, pathData).delete()
                    }
                } else {
                    if (currentFileInfo.compareTo(fileInfoView.text.toString()) != 0) {
                        withContext(Dispatchers.IO) {
                            saveFile(fileTitleView.text.toString())
                        }
                    }
                }
            }
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->{
                saveAndFinish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}