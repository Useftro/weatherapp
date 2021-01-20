package com.uniolco.weathapp.ui.weather.future.list

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.uniolco.weathapp.R
import com.uniolco.weathapp.data.db.converter.LocalDateConverter
import com.uniolco.weathapp.data.db.unitlocalized.future.list.UnitSpecificSimpleFutureWeatherEntry
import com.uniolco.weathapp.ui.base.RecyclerAdapterFavoriteList
import com.uniolco.weathapp.ui.base.RecyclerAdapterFutureList
import com.uniolco.weathapp.ui.base.ScopeFragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.favorite_list_weather_fragment.*
import kotlinx.android.synthetic.main.future_list_weather_fragment.*
import kotlinx.android.synthetic.main.future_list_weather_fragment.recyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import org.threeten.bp.LocalDate

class FutureListWeatherFragment : ScopeFragment(), KodeinAware {
    override val kodein by closestKodein()

    private lateinit var viewModel: FutureListWeatherViewModel
    private val viewModelFactory: FutureListWeatherViewModelFactory by instance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.future_list_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(FutureListWeatherViewModel::class.java)
        bindUI()
    }

    private fun bindUI() = launch(Dispatchers.Main) {
        val futureWeatherEntries = viewModel.weatherEntries.await()
        val weatherLocation = viewModel.weatherLocation.await()

        graphView.visibility = View.INVISIBLE

        weatherLocation.observe(viewLifecycleOwner, Observer { location ->
            if (location == null){
                return@Observer
            }
            updateLocation(location.name)
        })

        futureWeatherEntries.observe(viewLifecycleOwner, Observer { weatherEntries ->
            if (weatherEntries == null)
                return@Observer
            group_loading.visibility = View.GONE
            updateDate(weatherEntries[0].date, weatherEntries[weatherEntries.lastIndex].date)
            initRecyclerView(weatherEntries)
            updateGraph(weatherEntries)

        })
    }

    private fun updateLocation(name: String) {
        (activity as? AppCompatActivity)?.supportActionBar?.title = name
    }

    private fun updateDate(startDate: LocalDate, endDate: LocalDate){
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle =
            "${startDate.dayOfMonth}.${startDate.month} - ${endDate.dayOfMonth}.${endDate.month}"
    }

    private lateinit var adapterRec: RecyclerAdapterFutureList

    private fun initRecyclerView(items: List<UnitSpecificSimpleFutureWeatherEntry>){
        recyclerView.layoutManager = LinearLayoutManager(this@FutureListWeatherFragment.context)
        adapterRec = RecyclerAdapterFutureList(items.toMutableList())
        recyclerView.adapter = adapterRec
    }

    private fun updateGraph(list: List<UnitSpecificSimpleFutureWeatherEntry>){
        graphView.removeAllSeries()
        graphView.visibility = View.VISIBLE
        val line = mutableListOf<DataPoint>()
        list.forEach {
            line += DataPoint(it.date.dayOfMonth.toDouble(), it.averageTemp)
        }
        val series = LineGraphSeries<DataPoint>(line.toTypedArray())
        graphView.addSeries(series)
        graphView.title = getString(R.string.legendForChart, list.size.toString(), list[0].date.month.name)
    }


}