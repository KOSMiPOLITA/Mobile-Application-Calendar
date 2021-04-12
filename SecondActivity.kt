package com.example.zad1_141249

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.util.*

fun daysBetween(date: LocalDate, date2: LocalDate): Int {
    var p = date
    var k = date2
    var daysBetween = 0
    while (p.equals(k) == false) {
        k = k.minusDays(1)
        daysBetween += 1
    }
    Log.i("inf141249","Obliczenie dni pomiędzy datami")
    return daysBetween
}

fun ifHoliday(date: LocalDate): Boolean {
    Log.i("inf141249","Sprawdzanie czy data jest świętem, bądź weekendem")
    when (date.dayOfWeek) {
        DayOfWeek.SATURDAY -> {
            return false
        }
        DayOfWeek.SUNDAY -> {
            return false
        }
    }
    Log.i("inf141249","Nie jest weekendem")
    when (date.month) {
         Month.JANUARY -> {
             if ((date.dayOfYear == 1) or (date.dayOfYear == 6)) {
                 return false
             }
         }
        Month.MAY -> {
            if ((date.dayOfYear == 1) or (date.dayOfYear == 3)) {
                return false
            }
        }
        Month.AUGUST -> {
            if (date.dayOfYear == 15) {
                return false
            }
        }
        Month.NOVEMBER -> {
            if (date.dayOfYear == 1) {
                return false
            }
        }
        Month.DECEMBER -> {
            if ((date.dayOfYear == 25) or (date.dayOfYear == 26)) {
                return false
            }
        }
    }
    Log.i("inf141249","Nie jest świętem stałym")
    var (day, month) = whenWielkanoc(date.year)
    var rok = date.year
    var w = LocalDate.parse("$rok-$month-$day")
    w = w.plusDays(1)
    if (w.equals(date)) {
        return false
    }
    val (day2, month2) = whenBozeCialo(day.toInt(), month.toInt(), rok)
    if (LocalDate.parse("$rok-$month2-$day2").equals(date)) {
        return false
    }
    Log.i("inf141249","Nie jest świętem przechodnim")
    return true
}

fun robBeetwen(date: LocalDate, date2: LocalDate): Int {
    var p = date
    var k = date2
    var daysBetween = 0
    while (p.equals(k) == false) {
        if (ifHoliday(k) == true) {
            daysBetween += 1
        }
        k = k.minusDays(1)
    }
    Log.i("inf141249","Obliczenie roboczych dni pomiędzy datami")
    return daysBetween
}

class SecondActivity : AppCompatActivity() {

    var dateFirst = Calendar.getInstance()
    var dateLast = Calendar.getInstance()
    var dniKalendarzowe = 0
    var dniRobocze = 0
    var dP = (Calendar.getInstance()[Calendar.DAY_OF_MONTH]).toInt()
    var mP = (Calendar.getInstance()[Calendar.MONTH]).toInt()+1
    var yP = (Calendar.getInstance()[Calendar.YEAR]).toInt()
    var dK = (Calendar.getInstance()[Calendar.DAY_OF_MONTH]).toInt()
    var mK = (Calendar.getInstance()[Calendar.MONTH]).toInt()+1
    var yK = (Calendar.getInstance()[Calendar.YEAR]).toInt()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val textKal: TextView = findViewById(R.id.textKal)
        val textRob: TextView = findViewById(R.id.textRob)

        val date_begin: DatePicker = findViewById(R.id.datyPocz)
        date_begin.setOnDateChangedListener{ view, year, monthOfYear, dayOfMonth ->
            dP = dayOfMonth
            mP = monthOfYear+1
            yP = year
            Log.i("inf141249","Data początkowa zmieniona")
            if (porownajDaty(dP, mP, yP, dK, mK, yK) == true) {
                Log.i("inf141249","Daty są poprawnie ustawione")
                val (d1, m1) = przypiszDate(dK, mK)
                var finDate = LocalDate.parse("$yK-$m1-$d1")
                val (d, m) = przypiszDate(dP, mP)
                var begDate = LocalDate.parse("$yP-$m-$d")
                dniKalendarzowe = daysBetween(begDate, finDate)
                textKal.text = dniKalendarzowe.toString()
                dniRobocze = robBeetwen(begDate, finDate)
                textRob.text = dniRobocze.toString()
            }
            else {
                Log.i("inf141249","Daty błędnie ustawione")
                dniKalendarzowe = 0
                textKal.text = dniKalendarzowe.toString()
                dniRobocze = 0
                textRob.text = dniRobocze.toString()
            }
        }

        val date_end: DatePicker = findViewById(R.id.datyKoniec)
        date_end.setOnDateChangedListener{ view, year, monthOfYear, dayOfMonth ->
            dK = dayOfMonth
            mK = monthOfYear+1
            yK = year
            Log.i("inf141249","Data końcowa zmieniona")
            if (porownajDaty(dP, mP, yP, dK, mK, yK) == true) {
                Log.i("inf141249","Daty są poprawnie ustawione")
                val (d1, m1) = przypiszDate(dK, mK)
                var finDate = LocalDate.parse("$yK-$m1-$d1")
                val (d, m) = przypiszDate(dP, mP)
                var begDate = LocalDate.parse("$yP-$m-$d")
                dniKalendarzowe = daysBetween(begDate, finDate)
                textKal.text = dniKalendarzowe.toString()
                dniRobocze = robBeetwen(begDate, finDate)
                textRob.text = dniRobocze.toString()
            }
            else {
                Log.i("inf141249","Daty błędnie ustawione")
                dniKalendarzowe = 0
                textKal.text = dniKalendarzowe.toString()
                dniRobocze = 0
                textRob.text = dniRobocze.toString()
            }
        }
        val buttonBack = findViewById<Button>(R.id.buttonBack)
        buttonBack.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            Log.i("inf141249","Powrót do MAIN")
            startActivity(intent)
        }

    }

    fun porownajDaty(d1: Int, m1: Int, y1: Int, d2: Int, m2: Int, y2: Int): Boolean {
        Log.i("inf141249","Porównywanie dat")
        if (((y1 < y2) or ((y1 == y2) and (m1 < m2)) or ((y1 == y2) and (m1 == m2) and (d1 < d2))) and (d1 != 0) and (d2 != 0)) {
            return true
        }
        return false
    }
}