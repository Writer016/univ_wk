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

class WriteActivityProfile : AppCompatActivity() { //프로필 메모 작성 페이지
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

        //<< 메모 불러오기 LOGIC >>
        pathData = intent.getStringExtra("pathData").toString() //pathData는 '새 파일 생성'인지 '파일 수정'인지 경로를 알려준다.
        if(pathData!="null"){ //pathData가 존재 = 파일 수정 경로
            //pathData가 존재하지 않으면 새 파일 생성 경로이므로, 이 if문은 실행 안됨.
            //이 if문의 존재 이유는 메모 수정 시 저장된 내용 불러오기를 위함.
            try {
                //${filesDir}/${pathData}: 클릭한 메모 파일의 경로
                val bufferedReader = BufferedReader(FileReader("${filesDir}/${pathData}"))
                //FileReader(Path): 클릭한 메모 파일의 경로로부터 문자 기반 텍스트 파일 읽어옴. 파일이 존재하지 않으면 FileNotFoundException 발생

                var str: String?
                val builder = StringBuilder() //메모 내용 위한 StringBuilder
                val builderToCpr: StringBuilder = StringBuilder() //내용을 포함한 파일의 전체 텍스트(이미지 경로, 블로그, 이메일)를 위한 StringBuilder
                var i = 0

                while (true) {
                    str = bufferedReader.readLine() //한 줄씩 읽기
                    if (str == null) { break } //더 이상의 개행이 없으면 반복문 나오기
                    when (i) {
                        0 -> { imagePathData = str} //첫번째 줄은 이미지 경로.
                        1 -> { fileBlogView.setText(str) } //두번째 줄은 블로그.
                        2 -> { fileEmailView.setText(str) } //세번째 줄은 이메일.
                        else -> { builder.append(str).append("\n") } } //다음 줄 부터는 모두 메모 내용.
                    builderToCpr.append(str).append("\n") //첫번째 줄부터 끝까지 전체 텍스트
                    i++
                }
                bufferedReader.close() //bufferedReader 닫기

                currentFileInfo = builder.toString() //스트링빌더로 이어붙인 메모 내용
                fileAllText = builderToCpr.toString() //스트링빌더로 이어붙인 전체 내용

                fileNameView.setText("${pathData.subSequence(0, pathData.length - 5)}") //타이틀 텍스트뷰로 가져오기. 단, 뒤쪽 "p.txt"는 제외시킴.
                fileInfoView.setText(currentFileInfo) //불러온 파일 텍스트를 텍스트뷰로 가져오기
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

    //<< 파일 저장 LOGIC >>
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

    //<< 파일 이름 생성 LOGIC >>
    private fun generateName(title: String): String{
        var newTitle = title
        var index = 1

        //'title'+'p.txt' 이름의 파일 존재 여부 확인
        if(File(filesDir, "${title}p.txt").exists()) { //똑같은 이름의 파일이 존재하면,
            while(true){
                if(File(filesDir, "${title}-${index}p.txt").exists()){ //title-1부터 오름차순으로 title-2, title-3 ... 이름 존재하는지 확인. 방금 확인한 파일 이름이 존재하면,
                    index++ //title-n의 숫자 'n'에 1 더하기
                }else{ //방금 확인한 파일 이름이 존재하지 않으면,
                    newTitle = "${title}-${index}" //방금 확인한 파일 이름 title-n으로 최종 타이틀 확정
                    break //반복문 빠져나오기
                }
            }
        }
        return newTitle //최종 타이틀 반환.
    }

    //<< 파일 이름 생성 & 파일 저장 LOGIC >>
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
            android.R.id.home->{ //왼쪽 상단의 저장 버튼이 클릭되면..
                saveAndFinish() // <<파일 이름 생성>> 및 <<파일 저장>> LOGIC 실행
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