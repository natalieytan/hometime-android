package au.com.realestate.hometime.datastore

import android.content.Context
import android.content.SharedPreferences
import au.com.realestate.hometime.HomeTimeApplication

interface TokenDataStoreType {
    fun getTramTrackerToken(): String?
    fun setTramTrackerToken(token: String)
    fun removeTramTrackerToken()
}

class TokenDataStore private constructor(): TokenDataStoreType {
    companion object {
        private const val DATA_STORE_FILE_NAME: String = "homeTimeTokenDataStore"
        private const val KEY_TRAM_TRACKER_TOKEN: String = "KEY_TRAM_TRACKER_TOKEN"

        val instance: TokenDataStore by lazy {
            TokenDataStore()
        }
    }

    private val sharedPreferences: SharedPreferences

    init {
        sharedPreferences = HomeTimeApplication.context.getSharedPreferences(DATA_STORE_FILE_NAME, Context.MODE_PRIVATE)
    }

    override fun getTramTrackerToken(): String? {
        return sharedPreferences.getString(KEY_TRAM_TRACKER_TOKEN, null)
    }

    override fun setTramTrackerToken(token: String) {
        sharedPreferences.edit().putString(KEY_TRAM_TRACKER_TOKEN, token).apply()
    }

    override fun removeTramTrackerToken() {
        sharedPreferences.edit().remove(KEY_TRAM_TRACKER_TOKEN).apply()
    }
}