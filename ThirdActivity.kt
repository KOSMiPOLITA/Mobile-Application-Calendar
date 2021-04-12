package com.example.zad1_141249

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList
import kotlinx.android.synthetic.main.activity_third.*
import java.time.Month
import java.time.format.DateTimeFormatter


fun createDataList(y: Int): ArrayList<LocalDate> {
    Log.i("inf141249","Tworzenie listy do wyszukania niedziel")
    var lista: ArrayList<LocalDate> = ArrayList()
    lista.add(LocalDate.parse("$y-01-31"))
    val (day, month) = whenWielkanoc(y)
    lista.add(LocalDate.parse("$y-$month-$day"))
    lista.add(LocalDate.parse("$y-04-30"))
    lista.add(LocalDate.parse("$y-06-30"))
    lista.add(LocalDate.parse("$y-08-31"))
    lista.add(LocalDate.parse("$y-12-18"))
    lista.add(LocalDate.parse("$y-12-25"))
    return lista
}

fun addNiedziela(d: LocalDate): LocalDate {
    var work = 1
    var curData = d
    if (curData.month == Month.DECEMBER || (curData.month == Month.MARCH) || (curData.month == Month.APRIL && curData.dayOfMonth < 23)) {
        curData = curData.minusDays(1)
    }
    while (work == 1) {
        if (curData.dayOfWeek == DayOfWeek.SUNDAY) work = 0
        else curData = curData.minusDays(1)
    }
    Log.i("inf141249","Znalezenie konkretnej niedzieli")
    return curData
}

fun changeDateFormat(d: LocalDate): String {
    var tmp = d.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
    return tmp
}


fun monthToPolish(n: LocalDate): String {
    when (n.month) {
        Month.JANUARY -> return "Styczeń"
        Month.FEBRUARY -> return "Luty"
        Month.MARCH -> return "Marzec"
        Month.APRIL -> return "Kwiecień"
        Month.MAY -> return "Maj"
        Month.JULY -> return "Czerwiec"
        Month.JUNE -> return "Lipiec"
        Month.AUGUST -> return "Sierpień"
        Month.SEPTEMBER -> return "Wrzesień"
        Month.OCTOBER -> return "Październik"
        Month.NOVEMBER -> return "Listopad"
        Month.DECEMBER -> return "Grudzień"
    }
}

class ThirdActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("inf141249","Niedziele - onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)

        val df = (Calendar.getInstance()[Calendar.YEAR]).toInt()
        val rokzmain = intent.getIntExtra("Rok_akt", df)

        var niedzielaList: ArrayList<Niedziela> = ArrayList()
        var possibleDates = createDataList(rokzmain)

        for (i in possibleDates) {
            Log.i("inf141249","Szukanie niedziel handlowych")
            niedzielaList.add(Niedziela(monthToPolish(addNiedziela(i)), changeDateFormat(addNiedziela(i))))
        }

        val recyclerAdapter = RecyclerAdapter(niedzielaList)
        sundaysList.layoutManager = LinearLayoutManager(this)
        sundaysList.adapter = recyclerAdapter

        val buttonBack = findViewById<Button>(R.id.buttonBackNdz)
        buttonBack.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            Log.i("inf141249","Powrót do MAIN")
            startActivity(intent)
        }
    }
}