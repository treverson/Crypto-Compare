package com.shapps.cryptocompare.Model

import android.content.SharedPreferences
import android.preference.SwitchPreference
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.StringRequest
import com.shapps.cryptocompare.Activities.Main
import com.shapps.cryptocompare.Networking.AppController
import com.shapps.cryptocompare.Networking.DetailURLs
import org.json.JSONArray
import org.json.JSONObject
import java.util.ArrayList
import java.util.HashMap
import android.R.id.edit



/**
 * Created by shyam on 23/7/17.
 */

object LiveDataContent {
    /**
     * Shared Preferences File Name
     */
    private const val PREF_FILE = "ExchangesList"
    /**
     * An array of sample items.
     */
    val ITEMS: MutableList<LiveData> = ArrayList()

    /**
     * A map of sample items, by ID.
     */
    private val ITEM_MAP: MutableMap<String, LiveData> = HashMap()

    init {
        // Add some sample items.
    }

    private fun addItem(item: LiveData) {
        ITEMS.add(item)
        ITEM_MAP.put(item.id, item)
    }

    class LiveData(val id: String, val cryptoCurrency: String, val currency: String, val exchangeId: String, val exchangeName: String,
                   val priceBuy: String, val priceSell: String, val volume: String, val lowBuy: String, val highBuy: String,
                   val lowSell: String, val highSell: String)

    fun getData(activityContext: Main, currentExchanges: String) {
        Log.d("Load", "Ing")

        val url = DetailURLs.URL_GET_CURRENT + currentExchanges

        val strReq = StringRequest(Request.Method.GET,
                url, Response.Listener { response ->
            var currentData = JSONArray(response)
            for(i in 0 until currentData.length()){
                var exchangeCurrent = JSONObject(currentData.get(i).toString())
                var cryptoCurr = exchangeCurrent.getString("crypto_curr")
                var currency = exchangeCurrent.getString("curr")
                var exchangeId = exchangeCurrent.getString("exchange_id")
                var priceBuy = exchangeCurrent.getString("buy")
                var priceSell = exchangeCurrent.getString("sell")
                var timeInt = 1
                var volume = "100"
                var lowBuy = ""
                var highBuy = ""
                var lowSell = ""
                var highSell = ""
                when (timeInt) {
                    1 -> {
                        lowBuy = exchangeCurrent.getString("last_hour_min_buy")
                        highBuy = exchangeCurrent.getString("last_hour_max_buy")
                        lowSell = exchangeCurrent.getString("last_hour_min_sell")
                        highSell = exchangeCurrent.getString("last_hour_max_sell")
                    }
                    2 -> {
                        lowBuy = exchangeCurrent.getString("last_day_min_buy")
                        highBuy = exchangeCurrent.getString("last_day_max_buy")
                        lowSell = exchangeCurrent.getString("last_day_min_sell")
                        highSell = exchangeCurrent.getString("last_day_max_sell")
                    }
                    3 -> {
                        lowBuy = exchangeCurrent.getString("last_week_min_buy")
                        highBuy = exchangeCurrent.getString("last_week_max_buy")
                        lowSell = exchangeCurrent.getString("last_week_min_sell")
                        highSell = exchangeCurrent.getString("last_week_max_sell")
                    }
                    4 -> {
                        lowBuy = exchangeCurrent.getString("last_month_min_buy")
                        highBuy = exchangeCurrent.getString("last_month_max_buy")
                        lowSell = exchangeCurrent.getString("last_month_min_sell")
                        highSell = exchangeCurrent.getString("last_month_max_sell")
                    }
                }


                var sharedPref = activityContext.getSharedPreferences(PREF_FILE, 0)
                var name = sharedPref.getString(exchangeId, "No Name Found")

                addItem(LiveData(i.toString(), cryptoCurr, currency , exchangeId,
                        name, priceBuy, priceSell, volume, lowBuy, highBuy, lowSell, highSell))
            }
            Log.d("currentData : ", currentData.toString())
            Log.d("Load", "Done")
        }, Response.ErrorListener { error ->
            VolleyLog.d("TAG ", "Error: " + error.message)
        })

        // Adding request to request queue
        AppController.instance?.addToRequestQueue(strReq, "APPLE", activityContext)

    }
}
