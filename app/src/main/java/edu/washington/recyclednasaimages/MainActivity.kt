package edu.washington.recyclednasaimages

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_main.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

const val NUMBER_OF_DAYS = 30

class MainActivity : AppCompatActivity() {
    var astronomyItems = mutableListOf<AstronomyData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        populateAstronomyDates()
    }

    private fun populateAstronomyDates() {
        for (i in 1..NUMBER_OF_DAYS) {
            // Accommodate NASA APOD date formatting
            var dateString = getString(R.string.date_base) + i.toString().padStart(2, '0')

            val request = JsonObjectRequest(
                Request.Method.GET,
                nasaUriBuilder(dateString),
                null,
                Response.Listener { response ->
                    astronomyItems.add(parseAstronomyData(response))
                    (list_view_astronomy_data.adapter as ArrayAdapter<AstronomyData>).notifyDataSetChanged()},
                Response.ErrorListener { error ->
                    val toast = Toast.makeText(this, "$error: ${error.message.toString()}", Toast.LENGTH_SHORT)
                    toast.show()
                })

            NasaContentDownloader.requestQueue(applicationContext).add(request)
        }

        list_view_astronomy_data.adapter = AstronomyAdapter(this, astronomyItems)
    }

    private fun nasaUriBuilder(date: String): String? {
        val builder = Uri.Builder()
        builder.scheme("https").authority(getString(R.string.url_base))

        for (s in resources.getStringArray(R.array.url_paths)) {
            builder.appendPath(s)
        }

        builder.appendQueryParameter("api_key", getString(R.string.api_key))
            .appendQueryParameter("date", date)

        return builder.toString()
    }

    inner class AstronomyAdapter(c: Context, data: MutableList<AstronomyData>) :
        ArrayAdapter<AstronomyData>(c, R.layout.astronomy_item, data) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var updatedView = convertView
            if (updatedView == null) {
                updatedView = layoutInflater.inflate(R.layout.astronomy_item, parent, false)
            }

            updatedView!!.findViewById<TextView>(R.id.title)!!.text = (getItem(position) as AstronomyData).title
            return updatedView as View
        }
    }
}
