package com.example.guru2022

import android.app.DatePickerDialog
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.widget.doBeforeTextChanged
import java.util.*

class infoFeelim : AppCompatActivity() {

    var dateString = ""
    var starNum = 0.0
    var genreS = 0
    var placeS = 0

    lateinit var logo: View
    lateinit var addFeelim: TextView

    lateinit var edtMovieTitle: EditText
    lateinit var edtStartDate: TextView
    lateinit var edtFinishDate: TextView
    lateinit var edtGenre: Spinner
    lateinit var edtScore: RatingBar
    lateinit var edtPlace: Spinner

    lateinit var FdbMovieTitle: String
    lateinit var FdbStartDate: String
    lateinit var FdbFinishDate: String
    var FdbGenre: Int = 0
    lateinit var FdbScore: String
    var FdbPlace: Int = 0

    lateinit var myHelper: DBManager
    lateinit var sqlDB: SQLiteDatabase



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_feelim)

        logo = findViewById(R.id.logo)
        addFeelim = findViewById(R.id.addFeelim)

        edtMovieTitle = findViewById(R.id.edtMovieTitle)
        edtStartDate = findViewById(R.id.edtStartDate)
        edtFinishDate = findViewById(R.id.edtFinishDate)
        edtGenre = findViewById(R.id.edtGenre)
        edtScore = findViewById(R.id.edtScore)
        edtPlace = findViewById(R.id.edtPlace)

        val intent = intent
        FdbMovieTitle = intent.getStringExtra("intent_name").toString()

        // DB
        myHelper = DBManager(this, "movieList", null, 1)
        sqlDB = myHelper.readableDatabase

        var cursor: Cursor
        cursor = sqlDB.rawQuery("SELECT * FROM movieList WHERE mvTitle = '" + FdbMovieTitle + "';", null)

        if (cursor.moveToNext()) {
            FdbStartDate = cursor.getString(cursor.getColumnIndex("mvStartDate")).toString()
            FdbFinishDate = cursor.getString(cursor.getColumnIndex("mvFinishDate")).toString()
            FdbGenre = cursor.getInt(cursor.getColumnIndex("mvGenre")).toInt()
            FdbPlace = cursor.getInt(cursor.getColumnIndex("mvPlace")).toInt()
        }

        cursor.close()
        sqlDB.close()
        myHelper.close()

        edtMovieTitle.setText(FdbMovieTitle)
        edtStartDate.text = FdbStartDate
        edtFinishDate.text = FdbFinishDate


        // 로고 (클릭 시 메인 화면으로 이동)
        // 위치만 수정 필요 (Home)
        logo.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // 시작 날짜
        edtStartDate.setOnClickListener {
            val cal = Calendar.getInstance()    //캘린더뷰 만들기
            val dateSetListener =
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    dateString = "${year}.${month + 1}.${dayOfMonth}"
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
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    dateString = "${year}.${month + 1}.${dayOfMonth}"
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
        var DataG = resources.getStringArray(R.array.genreList)
        var adapterGenre = ArrayAdapter<String> (this, R.layout.spinnerlayout, DataG)
        edtGenre.adapter = adapterGenre

        edtGenre.setSelection(FdbGenre)

        edtGenre.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                genreS = position
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        // 별점
        edtScore.setOnRatingBarChangeListener { _, rating, _ ->
            edtScore.rating = rating
        }

        // 장소

        var DataP = resources.getStringArray(R.array.placeList)
        var adapterPlace = ArrayAdapter<String> (this, R.layout.spinnerlayout, DataP)

        edtPlace.adapter = adapterPlace

        edtPlace.setSelection(FdbPlace)

        edtPlace.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                placeS = position
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }


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
}