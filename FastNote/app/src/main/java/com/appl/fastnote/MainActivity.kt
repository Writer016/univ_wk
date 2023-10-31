package com.appl.fastnote

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.fastnote.R
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() { //메인액티비티 -- 홈 화면 제어
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    lateinit var context: Context
    private var fragArgs: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        context = this

        val normalFragment = NormalFragment()
        val profileFragment = ProfileFragment()

        drawerLayout = findViewById(R.id.DrawerLayout)

        val toolbar = findViewById<Toolbar>(R.id.Toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.mipmap.ic_ham)

        supportActionBar?.title="Notes"
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayout, normalFragment)
            commit()
        }

        //드로어 제어
        navigationView = findViewById(R.id.NavigationView)
        navigationView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.item1 ->{ //드로어 첫번째 메뉴 item 선택 시..
                    fragArgs = 1
                    supportActionBar?.title="Notes"
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.frameLayout, normalFragment) //페이지 1로 전환
                        commit()
                    }
                    drawerLayout.closeDrawer(GravityCompat.START)}
                R.id.item2 ->{ //드로어 두번째 메뉴 item 선택 시..
                    fragArgs = 2
                    supportActionBar?.title="Profiles"
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.frameLayout, profileFragment) //페이지 2로 전환
                        commit()
                    }
                    drawerLayout.closeDrawer(GravityCompat.START)}
            }
            true
        }
    }

    //새 메모 버튼 클릭 시..
    fun createNew(view: View){
        when(fragArgs){
            1->{ //일반 메모
                val intent = Intent(context, WriteActivityNormal::class.java)
                startActivity(intent)
            }
            2->{ //프로필 메모
                val intent = Intent(context, WriteActivityProfile::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->{
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}