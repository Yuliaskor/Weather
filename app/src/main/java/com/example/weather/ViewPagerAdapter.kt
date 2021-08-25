package com.example.weather

import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class ViewPagerAdapter(private val city: List<String>) :
    RecyclerView.Adapter<ViewPagerAdapter.Pager2ViewHolder>() {

    val API: String = "5d2571ddec1822665d64bd4f8e0915a5"

    inner class Pager2ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemCity: TextView = itemView.findViewById(R.id.address)
        val loader: ProgressBar = itemView.findViewById(R.id.loader)
        val mainContainer: RelativeLayout = itemView.findViewById(R.id.mainContainer)
        val errorText: TextView = itemView.findViewById(R.id.errorText)
        val updateAtView: TextView = itemView.findViewById(R.id.update_at)
        val statusView: TextView = itemView.findViewById(R.id.status)
        val tempView: TextView = itemView.findViewById(R.id.temp)
        val tempMinView: TextView = itemView.findViewById(R.id.temp_min)
        val tempMaxView: TextView = itemView.findViewById(R.id.temp_max)
        val sunriseView: TextView = itemView.findViewById(R.id.sunrise)
        val sunsetView: TextView = itemView.findViewById(R.id.sunset)
        val windView: TextView = itemView.findViewById(R.id.wind)
        val humidityView: TextView = itemView.findViewById(R.id.humidity)
        val pressureView: TextView = itemView.findViewById(R.id.pressure)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewPagerAdapter.Pager2ViewHolder {
        return Pager2ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.activity_main_item, parent, false)
        )

    }


    override fun onBindViewHolder(holder: ViewPagerAdapter.Pager2ViewHolder, position: Int) {
        holder.itemCity.text = city[position]
        val adress: String = holder.itemCity.text as String
        weatherTask(
            adress,
            holder.loader,
            holder.mainContainer,
            holder.errorText,
            holder.updateAtView,
            holder.statusView,
            holder.tempView,
            holder.tempMinView,
            holder.tempMaxView,
            holder.sunriseView,
            holder.sunsetView,
            holder.windView,
            holder.humidityView,
            holder.pressureView
        ).execute()
    }
    inner class weatherTask(
        private var itemCity: String,
        private var loader: ProgressBar,
        private var mainContainer: RelativeLayout,
        private var errorText: TextView,
        private var update_atView: TextView,
        private var statusView: TextView,
        private var tempView: TextView,
        private var temp_minView: TextView,
        private var temp_maxView: TextView,
        private val sunriseView: TextView,
        private var sunsetView: TextView,
        private var windView: TextView,
        private var humidityView: TextView,
        private var pressureView: TextView
    ) : AsyncTask<String, Void, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
            loader.visibility = View.VISIBLE
            mainContainer.visibility = View.GONE
            errorText.visibility = View.GONE
        }

        override fun doInBackground(vararg params: String?): String? {
            var response: String?
            try {
                response =
                    URL("https://api.openweathermap.org/data/2.5/weather?q=$itemCity&units=metric&appid=$API").readText(
                        Charsets.UTF_8
                    )
            } catch (e: Exception) {
                response = null
                loader.visibility = View.GONE
                errorText.visibility = View.VISIBLE
                mainContainer.visibility = View.GONE
            }
            return response
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                val jsonObj = JSONObject(result)
                val main = jsonObj.getJSONObject("main")
                println("000002 $main")
                val sys = jsonObj.getJSONObject("sys")
                val wind = jsonObj.getJSONObject("wind")
                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)
                val updateAt: Long = jsonObj.getLong("dt")
                val updateAtText =
                    "Updated at :" + SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.GERMAN).format(
                        Date(updateAt * 1000)
                    )
                val temp = main.getString("temp") + "°C"
                val tempMin = "Min Temp: " + main.getString("temp_min") + "°C"
                val tempMax = "Max Temp: " + main.getString("temp_max") + "°C"
                println("000002 $tempMin and  $tempMax")
                val pressure = main.getString("pressure")
                val humidity = main.getString("humidity")
                val sunrise: Long = sys.getLong("sunrise")
                val sunset: Long = sys.getLong("sunset")
                println("LOOOL$sunset")
                val windSpeed = wind.getString("speed")
                val weatherDescription = weather.getString("description")
                val address = jsonObj.getString("name") + ", " + sys.getString("country")


                itemCity = address
                update_atView.text = updateAtText
                statusView.text = weatherDescription.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()
                    ) else it.toString()
                }
                tempView.text = temp
                temp_minView.text = tempMin
                temp_maxView.text = tempMax
                sunriseView.text =
                    SimpleDateFormat("HH:mm", Locale.GERMAN).format(
                        Date(sunrise * 1000)
                    )
                sunsetView.text =
                    SimpleDateFormat("HH:mm", Locale.GERMAN).format(Date(sunset * 1000))

                println( "LOOOOL"+SimpleDateFormat("HH:mm", Locale.GERMAN).format(Date(sunset * 1000)))

                windView.text = windSpeed
                humidityView.text = humidity
                pressureView.text = pressure

                loader.visibility = View.GONE
                mainContainer.visibility = View.VISIBLE
            } catch (e: Exception) {
                errorText.visibility = View.VISIBLE
                loader.visibility = View.GONE
                mainContainer.visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int {
        return city.size
    }


}