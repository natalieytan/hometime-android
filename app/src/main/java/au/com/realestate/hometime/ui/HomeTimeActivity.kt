package au.com.realestate.hometime.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import au.com.realestate.hometime.R
import au.com.realestate.hometime.network.DataResponse
import kotlinx.android.synthetic.main.activity_home_time.*

class HomeTimeActivity : AppCompatActivity() {
    private lateinit var viewModel: HomeTimeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =
            ViewModelProvider(this, HomeTimeViewModelFactory()).get(HomeTimeViewModel::class.java)
        setContentView(R.layout.activity_home_time)

        buttonErrorRetry.setOnClickListener { viewModel.refresh() }
        buttonClear.setOnClickListener { viewModel.clear() }
        buttonRefresh.setOnClickListener { viewModel.refresh() }
        swipeRefreshLayout.setOnRefreshListener { viewModel.refresh() }

        val adapter = HomeTimeAdapter()
        recyclerViewHomeTime.adapter = adapter

        viewModel.dataResponse.observe(this, Observer { dataResponse ->
            when (dataResponse) {
                is DataResponse.Loading -> {
                    when (swipeRefreshLayout.isRefreshing) {
                        true -> {
                            progressBarHomeTime.visibility = View.GONE
                            viewError.visibility = View.GONE
                            recyclerViewHomeTime.visibility = View.VISIBLE
                            swipeRefreshLayout.visibility = View.VISIBLE
                        }
                        else -> {
                            progressBarHomeTime.visibility = View.VISIBLE
                            viewError.visibility = View.GONE
                            recyclerViewHomeTime.visibility = View.GONE
                            swipeRefreshLayout.visibility = View.GONE
                        }
                    }
                }
                is DataResponse.Error -> {
                    progressBarHomeTime.visibility = View.GONE
                    viewError.visibility = View.VISIBLE
                    recyclerViewHomeTime.visibility = View.GONE
                    swipeRefreshLayout.visibility = View.GONE
                    swipeRefreshLayout.isRefreshing = false
                }
                is DataResponse.Success, is DataResponse.Cleared -> {
                    progressBarHomeTime.visibility = View.GONE
                    viewError.visibility = View.GONE
                    recyclerViewHomeTime.visibility = View.VISIBLE
                    swipeRefreshLayout.visibility = View.VISIBLE
                    swipeRefreshLayout.isRefreshing = false
                }
            }
        })

        viewModel.homeTimeItems.observe(this, Observer { trams ->
            adapter.submitList(trams)
        })
    }
}