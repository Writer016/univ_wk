package com.appl.fastnote

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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

class WriteActivityProfile : AppCompatActivity() {
    private lateinit var fileNameView: EditText
    private lateinit var fileBlogView: EditText
    private lateinit var fileEmailView: EditText
    private lateinit var fileInfoView: EditText
    private var currentFileInfo: String = ""
    private var fileAllText: String = ""
    private var fileText: String = ""
    private var pathData: String = ""
    private var imagePathData: String = "def"
    private var pressedTime: Long = 0

    private val mStartForResult: ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result: ActivityResult ->
        imagePathData = result.data?.data.toString()
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_profile)

        fileNameView = findViewById(R.id.profile_name)
        fileBlogView = findViewById(R.id.profile_blog)
        fileEmailView = findViewById(R.id.profile_email)
        fileInfoView = findViewById(R.id.profile_info)
        val toolbar = findViewById<Toolbar>(R.id.Toolbar_write_profile)
        setSupportActionBar(toolbar)

        //set actionbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_save)

        pathData = intent.getStringExtra("pathData").toString()
        if(pathData!="null"){
            try {
                val bufferedReader = BufferedReader(FileReader("${filesDir}/${pathData}"))
                var str: String?
                val builder = StringBuilder()
                val builderToCpr: StringBuilder = StringBuilder()
                var i = 0

                while (true) {
                    str = bufferedReader.readLine()
                    if (str == null) { break }
                    when (i) {
                        0 -> { imagePathData = str}
                        1 -> { fileBlogView.setText(str) }
                        2 -> { fileEmailView.setText(str) }
                        else -> { builder.append(str).append("\n") } }
                    builderToCpr.append(str).append("\n")
                    i++
                }
                bufferedReader.close()

                currentFileInfo = builder.toString()
                fileAllText = builderToCpr.toString()

                fileNameView.setText("${pathData.subSequence(0, pathData.length - 5)}")
                fileInfoView.setText(currentFileInfo)
            }
            catch(t: Throwable){
                Toast.makeText(
                    this,
                    R.string.readWarn,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_profile, menu)
        return true
    }

    private fun saveFile(fileTitle: String){
        try {
            val bufferedWriter = BufferedWriter(FileWriter("${filesDir}/${fileTitle}p.txt"))
            bufferedWriter.write(fileText)
            bufferedWriter.close()
        } catch (t: Throwable) {
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
        if(File(filesDir, "${title}p.txt").exists()) {
            while(true){
                if(File(filesDir, "${title}-${index}p.txt").exists()){
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
            if(fileNameView.text.toString().trim()=="") fileNameView.setText(R.string.newNote)
            if (pathData == "null") {
                withContext(Dispatchers.IO){
                    saveFile(generateName(fileNameView.text.toString()))
                }
            } else {
                if (pathData.compareTo("${fileNameView.text}p.txt") != 0) {
                    withContext(Dispatchers.IO) {
                        saveFile(generateName(fileNameView.text.toString()))
                        File(filesDir, pathData).delete()
                    }
                } else {
                    if (fileText.compareTo(fileAllText) != 0) {
                        withContext(Dispatchers.IO) {
                            saveFile(fileNameView.text.toString())
                        }
                    }
                }
            }
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        fileText = "${imagePathData}\n${fileBlogView.text}\n${fileEmailView.text}\n${fileInfoView.text}"
        when(item.itemId){
            android.R.id.home->{
                saveAndFinish()
            }
            R.id.profileMenu_item1 ->{ //default image
                imagePathData="def"
            }
            R.id.profileMenu_item2 ->{ //custom image
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = MediaStore.Images.Media.CONTENT_TYPE
                intent.type = "image/*"
                mStartForResult.launch(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}