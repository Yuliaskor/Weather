package com.example.weather

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import me.relex.circleindicator.CircleIndicator3

class MainActivity : AppCompatActivity() {

    private var cityList = mutableListOf<String>()
    private lateinit var view_pager2: ViewPager2
    private lateinit var indicator: CircleIndicator3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        view_pager2 = findViewById(R.id.view_pager2)
        indicator = findViewById(R.id.indicator)

        postToList()
        view_pager2.adapter = ViewPagerAdapter(cityList)
        view_pager2.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        indicator.setViewPager(view_pager2)



        view_pager2.apply {
            beginFakeDrag()
            fakeDragBy(-10f)
            endFakeDrag()
        }

    }

    private fun addToList(city: String){
        cityList.add(city)
    }
    private fun postToList(){
        addToList("vinnytsia,ua")
        addToList("kyiv,ua")
        addToList("wroclaw,pl")
    }
}