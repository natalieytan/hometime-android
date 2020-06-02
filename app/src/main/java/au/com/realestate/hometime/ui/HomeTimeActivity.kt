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

        viewModel.apiStatus.observe(this, Observer { apiStatus ->
            when (apiStatus.state) {
                ApiStatus.State.LOADING -> {
                    loader.visibility = View.VISIBLE
                    error.visibility = View.GONE
                    results.visibility = View.GONE
                    cleared.visibility = View.GONE
                }
                ApiStatus.State.ERROR -> {
                    loader.visibility = View.GONE
                    error.visibility = View.VISIBLE
                    results.visibility = View.GONE
                    cleared.visibility = View.GONE
                }
                ApiStatus.State.DONE -> {
                    loader.visibility = View.GONE
                    error.visibility = View.GONE
                    results.visibility = View.VISIBLE
                    cleared.visibility = View.GONE
                }
                ApiStatus.State.CLEARED -> {
                    loader.visibility = View.GONE
                    error.visibility = View.GONE
                    results.visibility = View.GONE
                    cleared.visibility = View.VISIBLE
                }
            }
        })

        viewModel.trams.observe(this, Observer { trams ->
            results.text = "These are the results $trams. I'll put in a pretty recycler view for you soon."
        })
    }
}