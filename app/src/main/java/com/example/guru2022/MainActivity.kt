package com.example.guru2022

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import java.util.*

class MainActivity : AppCompatActivity() {


    var dateString = ""

    lateinit var logo: View
    lateinit var addFeelim: TextView

    lateinit var edtMovieTitle: EditText
    lateinit var edtStartDate: TextView
    lateinit var edtFinishDate: TextView
    lateinit var edtGenre: Spinner
    lateinit var edtScore: RatingBar
    lateinit var edtPlace: Spinner

    lateinit var myHelper: myDBHelper
    lateinit var sqlDB: SQLiteDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        logo = findViewById(R.id.logo)
        addFeelim = findViewById(R.id.addFeelim)

        edtMovieTitle = findViewById(R.id.edtMovieTitle)
        edtStartDate = findViewById(R.id.edtStartDate)
        edtFinishDate = findViewById(R.id.edtFinishDate)
        edtGenre = findViewById(R.id.edtGenre)
        edtScore = findViewById(R.id.edtScore)
        edtPlace = findViewById(R.id.edtPlace)

        // DB
        myHelper = myDBHelper(this)

        // 로고 (클릭 시 메인 화면으로 이동)
        // 위치만 수정 필요 (Home)
        logo.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }

        // 시작 날짜
        edtStartDate.setOnClickListener {
            val cal = Calendar.getInstance()    //캘린더뷰 만들기
            val dateSetListener =
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    dateString = "${year % 100}년 ${month + 1}월 ${dayOfMonth}일"
                    edtStartDate.text = dateString
                }
            DatePickerDialog(
                this,
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // 종료 날짜
        edtFinishDate.setOnClickListener {
            val cal = Calendar.getInstance()    //캘린더뷰 만들기
            val dateSetListener =
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    dateString = "${year % 100}년 ${month + 1}월 ${dayOfMonth}일"
                    edtFinishDate.text = dateString
                }
            DatePickerDialog(
                this,
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // 장르
        edtGenre.adapter =
            ArrayAdapter.createFromResource(this, R.array.genreList, R.layout.spinnerlayout)

        // 별점
        edtScore.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            edtScore.rating = rating
        }

        // 장소
        edtPlace.adapter =
            ArrayAdapter.createFromResource(this, R.array.placeList, R.layout.spinnerlayout)


        // 정보 DB로 이동
        addFeelim.setOnClickListener {
            sqlDB = myHelper.writableDatabase

            sqlDB.execSQL("INSERT INTO movieList VALUES ('" + edtMovieTitle.text.toString() + "','"
                    + edtStartDate.text.toString() + "','"
                    + edtFinishDate.text.toString() + "','"
                    + edtGenre.toString() + "','"
                    + edtScore.toString() + "','"
                    + edtPlace.toString()
                    + "');") // DB에 저장 (제목, 시작날짜, 종료날짜, 장르, 평점, 장소/플랫폼)
            sqlDB.close()

        }

        }
        inner class myDBHelper(context: Context) : SQLiteOpenHelper(context, "movieList", null, 1) {
            override fun onCreate(db: SQLiteDatabase?) {
                db!!.execSQL("CREATE TABLE movieList (mvTitle CHAR(20) PRIMARY KEY, mvStartDate CHAR, mvFinishDate CHAR, mvGenre CHAR, mvScore CHAR, mvPlace CHAR);")    // 테이블 명 : groupTBL // PRIMARY KEY: 키 값 // ()안 열 추가
            }

            override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
                db!!.execSQL("DROP TABLE IF EXISTS movieList")   // 존재할 때만 삭제 : IF EXISTS 추가
                onCreate(db)
            }
        }
}
