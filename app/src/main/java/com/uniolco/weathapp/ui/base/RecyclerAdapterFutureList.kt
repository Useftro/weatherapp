package com.uniolco.weathapp.ui.base

import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.uniolco.weathapp.R
import com.uniolco.weathapp.data.db.converter.LocalDateConverter
import com.uniolco.weathapp.data.db.unitlocalized.future.list.UnitSpecificSimpleFutureWeatherEntry
import com.uniolco.weathapp.internal.glide.GlideApp
import com.uniolco.weathapp.internal.inflate
import com.uniolco.weathapp.ui.weather.future.list.FutureListWeatherFragmentDirections
import kotlinx.android.synthetic.main.item_future_weather.view.*
import org.threeten.bp.LocalDate
import java.util.*

class RecyclerAdapterFutureList(private val futureWeatherList: MutableList<UnitSpecificSimpleFutureWeatherEntry>):
    RecyclerView.Adapter<RecyclerAdapterFutureList.ItemLocationHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapterFutureList.ItemLocationHolder {
        val inflatedView = parent.inflate(R.layout.item_future_weather, false)
        return ItemLocationHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: RecyclerAdapterFutureList.ItemLocationHolder, position: Int) {
        val itemFavorite = futureWeatherList[position]
        holder.bindFavorite(itemFavorite)
    }

    override fun getItemCount(): Int {
        return futureWeatherList.size
    }


    inner class ItemLocationHolder(v: View): RecyclerView.ViewHolder(v), View.OnClickListener, View.OnLongClickListener {

        private var view: View = v
        private var future: UnitSpecificSimpleFutureWeatherEntry? = null

        init {
            v.setOnClickListener(this)
            v.setOnLongClickListener(this)
        }

        override fun onClick(v: View?) {
            navigateToWeatherDetail(future!!.date, view)
        }

        private fun navigateToWeatherDetail(date: LocalDate, view: View){
            val dateString = LocalDateConverter.dateToString(date)!!
            val actionDetail = FutureListWeatherFragmentDirections.actionDetailed(dateString)
            Navigation.findNavController(view).navigate(actionDetail)
        }

        fun bindFavorite(weatherEntry: UnitSpecificSimpleFutureWeatherEntry){
            this.future = weatherEntry
            view.textView_date.text = weatherEntry.date.toString()
            view.textView_cityName.text = view.context.getString(R.string.temperature, future!!.averageTemp.toString())
            view.textView_Humidity.text = view.context.getString(R.string.humidity, future!!.avgHumidity.toString())
            view.textView_wind.text = view.context.getString(R.string.windSpeed, future!!.maxWindSpeed)
            GlideApp.with(this.itemView).load("https:" + weatherEntry.conditionIcon).into(view.imageView_condition_icon)

        }

        override fun onLongClick(v: View?): Boolean {
            val sendIntent: Intent = Intent().apply {

                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "On " +
                        "${future!!.date.dayOfMonth} ${future!!.date.month}".toLowerCase(Locale.ROOT) +
                        " there will be: ${future!!.averageTemp}°C")
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            ContextCompat.startActivity(view.context, shareIntent, null)
            return true
        }
    }
}