package com.uniolco.weathapp.ui.weather.future.list

import com.uniolco.weathapp.R
import com.uniolco.weathapp.data.db.unitlocalized.future.list.UnitSpecificSimpleFutureWeatherEntry
import com.uniolco.weathapp.internal.glide.GlideApp
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_future_weather.*

class FutureWeatherItem(
    val weatherEntry: UnitSpecificSimpleFutureWeatherEntry
): Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.apply {
            textView_date.text = weatherEntry.date.toString()
            textView_avgTemperature.text = weatherEntry.averageTemp.toString()
            textView_Humidity.text = weatherEntry.avgHumidity.toString()
            GlideApp.with(this.containerView).load("https:" + weatherEntry.conditionIcon).into(imageView_condition_icon)
        }
    }

    override fun getLayout() = R.layout.item_future_weather


}