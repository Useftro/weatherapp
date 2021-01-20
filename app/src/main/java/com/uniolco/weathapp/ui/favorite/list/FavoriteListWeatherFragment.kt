package com.uniolco.weathapp.ui.favorite.list

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.uniolco.weathapp.R
import com.uniolco.weathapp.data.db.entity.favorite.Locations
import com.uniolco.weathapp.ui.base.RecyclerAdapterFavoriteList
import com.uniolco.weathapp.ui.base.ScopeFragment
import com.uniolco.weathapp.ui.base.SharedViewModel
import com.uniolco.weathapp.ui.base.SwipeToDeleteCallback
import kotlinx.android.synthetic.main.favorite_list_weather_fragment.*
import kotlinx.android.synthetic.main.favorite_list_weather_fragment.recyclerView
import kotlinx.android.synthetic.main.future_list_weather_fragment.*
import kotlinx.android.synthetic.main.item_favorite_list_weather.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.factory

class FavoriteListWeatherFragment : ScopeFragment(), KodeinAware {

    override val kodein by closestKodein()
    private lateinit var viewModel: FavoriteListWeatherViewModel
    private val viewModelFactoryInstanceFactory:
            ((String) -> FavoriteListWeatherViewModelFactory) by factory()
    private val model: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.favorite_list_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val email = model.email.value.toString()
        viewModel = ViewModelProvider(this, viewModelFactoryInstanceFactory(email)).get(FavoriteListWeatherViewModel::class.java)
        bindUI()
    }

    private fun bindUI() = launch {
        val favorites = viewModel.favorites.await()

        favorites.observe(viewLifecycleOwner, Observer { favorits ->
            if(favorits == null) return@Observer
            initRecyclerView(favorits)
        })
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapterRec.filter.filter(newText)
                return false
            }

        })
        updateTitle()
        updateSubtitle()
        deleteAll()
    }

    private fun updateTitle(){
        (activity as? AppCompatActivity)?.supportActionBar?.title = getString(R.string.favorite)
    }

    private fun updateSubtitle(){
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = ""
    }

    private suspend fun deleteAll(){
        clearAll_button.setOnClickListener {
            launch {
                viewModel.deleteAllLocations()
            }
            Toast.makeText(clearAll_button.context, getString(R.string.toastDeletedAllFavorite),
                Toast.LENGTH_LONG).show()
        }
    }

    private lateinit var adapterRec: RecyclerAdapterFavoriteList

    private fun initRecyclerView(items: List<Locations>){

        recyclerView.layoutManager = LinearLayoutManager(this@FavoriteListWeatherFragment.context)
        adapterRec = RecyclerAdapterFavoriteList(items.toMutableList())
        recyclerView.adapter = adapterRec

        val swipeHandler = object: SwipeToDeleteCallback(layoutInflater.context){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = recyclerView.adapter as RecyclerAdapterFavoriteList
                val loc = adapter.removeAt(viewHolder.adapterPosition)
                deleteLocation(loc)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun deleteLocation(locations: Locations){
        launch {
            viewModel.deleteLocation(locations)
        }
    }
}