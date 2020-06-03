package au.com.realestate.hometime

import android.app.Application
import android.content.Context

class HomeTimeApplication : Application() {
    companion object {
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}