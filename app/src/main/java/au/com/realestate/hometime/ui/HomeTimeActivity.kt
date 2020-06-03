package au.com.realestate.hometime.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import au.com.realestate.hometime.R
import au.com.realestate.hometime.network.ApiStatus
import kotlinx.android.synthetic.main.activity_home_time.*

class HomeTimeActivity : AppCompatActivity() {
    private lateinit var viewModel: HomeTimeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =
            ViewModelProvider(this, HomeTimeViewModelFactory()).get(HomeTimeViewModel::class.java)
        setContentView(R.layout.activity_home_time)

        buttonClear.setOnClickListener { viewModel.clear() }
        buttonRefresh.setOnClickListener { viewModel.refresh() }

        val adapter = HomeTimeAdapter()
        recyclerViewHomeTime.adapter = adapter

        viewModel.apiStatus.observe(this, Observer { apiStatus ->
            when (apiStatus.state) {
                ApiStatus.State.LOADING -> {
                    loader.visibility = View.VISIBLE
                    error.visibility = View.GONE
                    recyclerViewHomeTime.visibility = View.GONE
                }
                ApiStatus.State.ERROR -> {
                    loader.visibility = View.GONE
                    error.visibility = View.VISIBLE
                    recyclerViewHomeTime.visibility = View.GONE
                }
                ApiStatus.State.DONE, ApiStatus.State.CLEARED -> {
                    loader.visibility = View.GONE
                    error.visibility = View.GONE
                    recyclerViewHomeTime.visibility = View.VISIBLE
                }
            }
        })

        viewModel.trams.observe(this, Observer { trams ->
            adapter.submitList(trams)
        })
    }
}