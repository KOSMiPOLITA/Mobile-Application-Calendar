package com.example.zad1_141249

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.*

data class Data(var day: String, var month: String)

val rokNormalny = arrayOf(0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
val rokPrzestepny = arrayOf(0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)

fun przypiszDate(d: Int, m: Int): Data {
    var dzien = "0"
    var miesiac = "0"
    if (d < 10) {
        dzien = dzien + d.toString()
    }
    else {
        dzien = d.toString()
    }
    if (m < 10) {
        miesiac = miesiac + m.toString()
    }
    else {
        miesiac = m.toString()
    }
    Log.i("inf141249","Konwertowanie daty dla dni, miesięcy < 10")
    return Data(dzien, miesiac)
}

fun whenWielkanoc(year: Int): Data {
    var a = 0; var b = 0; var c = 0; var d = 0; var e = 0; var f = 0; var g = 0; var h = 0;
    var i = 0; var k = 0; var l = 0; var m = 0; var p = 0; var day = 0; var month = 0;
    a = year % 19
    b = (year / 100).toInt()
    c = year % 100
    d = (b / 4).toInt()
    e = b % 4
    f = ((b + 8) / 25).toInt()
    g = ((b - f + 1) / 3).toInt()
    h = (19 * a + b - d - g + 15) % 30
    i = (c / 4).toInt()
    k = c % 4
    l = (32 + 2 * e + 2 * i - h - k) % 7
    m = ((a + 11 * h + 22 * l) / 451).toInt()
    p = (h + l - 7 * m + 114) % 31
    day = p + 1
    month = ((h + l - 7 * m + 114) / 31).toInt()
    Log.i("inf141249","Znaleziono wielkanoc")
    var wynik = przypiszDate(day, month)
    return wynik
}

fun whenPopielec(d: Int, m: Int, y: Int): Data {
    var dniPopielca = 46
    var day = d; var month = m;
    while (dniPopielca > 0) {
        day = day - 1
        if (day == 0) {
            month = month - 1
            if (y % 4 == 0) {
                day = rokPrzestepny[month]
            }
            else {
                day = rokNormalny[month]
            }
        }
            dniPopielca = dniPopielca - 1
        }
    Log.i("inf141249","Znaleziono popielec")
    var wynik = przypiszDate(day, month)
    return wynik
}

fun whenBozeCialo(d: Int, m: Int, y: Int): Data {
    var dniBC = 60
    var day = d; var month = m;
    var rok = rokNormalny
    if (y % 4 == 0) {
        var rok = rokPrzestepny
    }
    while (dniBC > 0) {
        day = day + 1
        if (day > rok[month]) {
            month = month + 1
            day = 1
        }
        dniBC = dniBC - 1
    }
    Log.i("inf141249","Znaleziono Boże Ciało")
    var wynik = przypiszDate(day, month)
    return wynik
}

fun whenAdwent(y: Int): Data {
    var d = 25
    var m = 12
    var wigilia = LocalDate.parse("$y-12-25")
    var dayofweek = wigilia.dayOfWeek
    when (dayofweek) {
        DayOfWeek.MONDAY -> {
            d = d - 1
        }
        DayOfWeek.TUESDAY -> {
            d = d - 2
        }
        DayOfWeek.WEDNESDAY -> {
            d = d - 3
        }
        DayOfWeek.THURSDAY -> {
            d = d - 4
        }
        DayOfWeek.FRIDAY -> {
            d = d - 5
        }
        DayOfWeek.SATURDAY -> {
            d = d - 6
        }
        else -> {
            d = d - 7
        }
    }
    var dnidowigili = 21
    while (dnidowigili > 0) {
        d = d - 1
        if (d == 0) {
            m = m - 1
            d = 30
        }
        dnidowigili = dnidowigili - 1
    }
    Log.i("inf141249","Znaleziono początek adwentu")
    var wynik = przypiszDate(d, m)
    return wynik

}

class MainActivity : AppCompatActivity() {

    var rokMain = Calendar.getInstance()[Calendar.YEAR]

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dni_roboczeButton = findViewById<Button>(R.id.dni_roboczeButton)
        dni_roboczeButton.setOnClickListener{
            val intent = Intent(this, SecondActivity::class.java)
            Log.i("inf141249","Wybór activity - DNI ROBOCZE")
            startActivity(intent)
        }

        val niedzieleButton = findViewById<Button>(R.id.niedzieleButton)
        niedzieleButton.setOnClickListener{
            if (rokMain < 2020) {
                Toast.makeText(this, "Dla roków mniejszych niż 2020 funkcja jest wyłączona", Toast.LENGTH_SHORT)
                    .show()
            }
            else {
                val intent2 = Intent(this, ThirdActivity::class.java)
                intent2.putExtra("Rok_akt", rokMain)
                Log.i("inf141249","Wybór activity - NIEDZIELE HANDLOWE")
                startActivity(intent2)
            }

        }

        val popielec:TextView = findViewById(R.id.popielec)
        val wielkanoc:TextView = findViewById(R.id.wielkanoc)
        val boze:TextView = findViewById(R.id.boze)
        val adwent:TextView = findViewById(R.id.adwent)
        Log.i("inf141249","TextViews dla świąt")

        val year_picker: NumberPicker = findViewById(R.id.wybierzRok)
        year_picker.minValue = 1900
        year_picker.maxValue = 2200
        year_picker.wrapSelectorWheel = true
        year_picker.value = rokMain

        year_picker.setOnValueChangedListener { picker, oldVal, newVal ->
            Log.i("inf141249","Zmiana roku - szukanie świąt")
            val year = newVal
            rokMain = year
            val (day, month) = whenWielkanoc(year)
            val (day1, month1) = whenPopielec(day.toInt(), month.toInt(), year)
            val (day2, month2) = whenBozeCialo(day.toInt(), month.toInt(), year)
            val (day3, month3) = whenAdwent(year)
            wielkanoc.text = "$day.$month.$year"
            popielec.text = "$day1.$month1.$year"
            boze.text = "$day2.$month2.$year"
            adwent.text = "$day3.$month3.$year"
        }

    }
}