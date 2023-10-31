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

class WriteActivityNormal : AppCompatActivity() { //일반 메모 작성 페이지
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

        pathData = intent.getStringExtra("pathData").toString() //pathData는 '새 파일 생성'인지 '파일 수정'인지 경로를 알려준다.
        if(pathData!="null"){ //pathData가 존재 = 파일 수정 경로
            //pathData가 존재하지 않으면 새 파일 생성 경로이므로, 이 if문은 실행 안됨.
            //이 if문의 존재 이유는 메모 수정 시 저장된 내용 불러오기를 위함.
            try{
                //${filesDir}/${pathData}: 클릭한 메모 파일의 경로
                val bufferedReader = BufferedReader(FileReader("${filesDir}/${pathData}"))
                //FileReader(Path): 클릭한 메모 파일의 경로로부터 문자 기반 텍스트 파일 읽어옴. 파일이 존재하지 않으면 FileNotFoundException 발생

                var str: String?
                val builder = StringBuilder() //메모 내용 위한 StringBuilder

                while(true){
                    str=bufferedReader.readLine() //한 줄씩 읽기
                    if(str==null){break} //더 이상의 개행이 없으면 반복문 나오기
                    builder.append(str).append("\n") //한 줄씩 이어붙이기
                }
                bufferedReader.close() //bufferedReader 닫기

                currentFileInfo = builder.toString() //스트링빌더로 이어붙인 전체 내용
                fileTitleView.setText("${pathData.subSequence(0, pathData.length-5)}") //타이틀 텍스트뷰로 가져오기. 단, 뒤쪽 "n.txt"는 제외시킴.
                fileInfoView.setText(currentFileInfo) //불러온 파일 텍스트를 텍스트뷰로 가져오기
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

    //<< 파일 저장 >>
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

    //<< 파일 이름 생성 >>
    private fun generateName(title: String): String{
        var newTitle = title
        var index = 1

        //'title'+'n.txt' 이름의 파일 존재 여부 확인
        if(File(filesDir, "${title}n.txt").exists()) { //똑같은 이름의 파일이 존재하면,
            while(true){
                  if(File(filesDir, "${title}-${index}n.txt").exists()){ //title-1부터 오름차순으로 title-2, title-3 ... 이름 존재하는지 확인. 방금 확인한 파일 이름이 존재하면,
                            index++ //title-n의 숫자 'n'에 1 더하기
                        }else{ //방금 확인한 파일 이름이 존재하지 않으면,
                            newTitle = "${title}-${index}" //방금 확인한 파일 이름 title-n으로 최종 타이틀 확정
                            break //반복문 빠져나오기
                        }
                    }
                }
        return newTitle //최종 타이틀 반환.
    }

    //<< 파일 이름 생성 & 파일 저장 >>
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