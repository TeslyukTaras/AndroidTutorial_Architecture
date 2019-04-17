package com.teslyuk.weatherapp.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.teslyuk.weatherapp.R
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.teslyuk.weatherapp.ui.adapter.DailyWeatherAdapter
import com.teslyuk.weatherapp.ui.adapter.HourlyWeatherAdapter

class MainActivity : AppCompatActivity() {

    //day
    private lateinit var dayRecyclerView: RecyclerView
    lateinit var dayViewAdapter: DailyWeatherAdapter
    private lateinit var dayViewManager: RecyclerView.LayoutManager

    //hour
    private lateinit var hourRecyclerView: RecyclerView
    lateinit var hourViewAdapter: HourlyWeatherAdapter
    private lateinit var hourViewManager: RecyclerView.LayoutManager

    private val controller by lazy { MainController(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        initView()
    }

    private fun initView() {
        hourViewManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        hourViewAdapter = HourlyWeatherAdapter(listOf())

        hourRecyclerView = findViewById<RecyclerView>(R.id.hourlyRecyclerView).apply {
            setHasFixedSize(true)
            layoutManager = hourViewManager
            adapter = hourViewAdapter
        }

        dayViewManager = LinearLayoutManager(this)
        dayViewAdapter = DailyWeatherAdapter(listOf())

        dayRecyclerView = findViewById<RecyclerView>(R.id.dailyRecyclerView).apply {
            setHasFixedSize(true)
            layoutManager = dayViewManager
            adapter = dayViewAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        controller.fetchData()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add_city -> {
                controller.onChangeCityClick()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun showCityNameDialog() {
        val alert = AlertDialog.Builder(this)
        val editText = EditText(this)
        alert.setMessage("Select City")
        alert.setTitle("Enter Your city name")
        alert.setView(editText)
        alert.setPositiveButton("Search") { dialog, whichButton ->
            controller.onCitySelected(editText.text.toString())
        }

        alert.show()
    }

    fun showToast(errorCode: Int) {
        Toast.makeText(this, errorCode, Toast.LENGTH_LONG).show()
    }
}