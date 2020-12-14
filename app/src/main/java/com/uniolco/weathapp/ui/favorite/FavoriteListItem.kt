package com.uniolco.weathapp.ui.favorite

import com.uniolco.weathapp.R
import com.uniolco.weathapp.data.db.entity.favorite.Locations
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_favorite_list_weather.*
import kotlinx.android.synthetic.main.item_future_weather.*
import kotlinx.android.synthetic.main.item_future_weather.textView_cityName

class FavoriteListItem(val locations: Locations
): Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.apply {
            textView_cityName.text = "${locations.location.country} \n ${locations.location.name}"
        }
    }

    override fun getLayout() = R.layout.item_favorite_list_weather
}