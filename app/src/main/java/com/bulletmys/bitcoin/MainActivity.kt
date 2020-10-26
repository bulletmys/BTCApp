package com.bulletmys.bitcoin

import CryptoRepository
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bulletmys.bitcoin.Interface.RetrofitServices
import com.google.android.material.snackbar.Snackbar
import com.jaredrummler.materialspinner.MaterialSpinner
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var repository: CryptoRepository
    private lateinit var preferences: SharedPreferences
    private var crypto: String = "BTC"
    private var currency: String = "USD"
    private var limit: Int = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cryptoUrl = findViewById<TextView>(R.id.crypto_url)
        cryptoUrl.movementMethod = LinkMovementMethod.getInstance()

        val toolbar = findViewById<Toolbar>(R.id.toolbar_custom);
        setSupportActionBar(toolbar);

        val apiService = RetrofitServices.create()

        setupRecycler()
        setupSpinner()

        preferences = PreferenceManager.getDefaultSharedPreferences(this)
        preferences.registerOnSharedPreferenceChangeListener(this)

        updPreferences()

        repository = CryptoRepository(apiService)
        refreshAndSetRecycler(repository)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.settings_menu, menu);
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_settings) {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
        if (item.itemId == R.id.action_renew) {
            refreshAndSetRecycler(repository)
        }
        return true
    }

    private fun setupSpinner() {
        val spinner =
            findViewById<View>(R.id.spinner) as MaterialSpinner
        spinner.setItems(
            "BTC",
            "ETH",
            "LTC"
        )

        spinner.setOnItemSelectedListener { view, position, id, item ->
            crypto = item as String
            refreshAndSetRecycler(repository)
        }
    }

    private fun setupRecycler() {
        viewManager = LinearLayoutManager(this)
        viewAdapter =
            BTCAdapter(
                BTCDataSet = ArrayList(1),
                onElemClickListener = { item -> onListClickListener(item) })

        recyclerView = findViewById<RecyclerView>(R.id.btc_recycler).apply {
            setHasFixedSize(true)

            layoutManager = viewManager
            adapter = viewAdapter
        }

        val dividerItemDecoration = DividerItemDecoration(
            recyclerView.context,
            (viewManager as LinearLayoutManager).orientation
        )
        recyclerView.addItemDecoration(dividerItemDecoration)
    }

    private fun onListClickListener(item: BTCListItem) {
        Snackbar.make(
            this.recyclerView,
            "Min:${item.minPrice} Max:${item.maxPrice}",
            Snackbar.LENGTH_INDEFINITE
        ).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        preferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    private fun refreshAndSetRecycler(repository: CryptoRepository) {
        repository.getCryptoInfo(crypto, currency, limit)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({ result ->
                val data: ArrayList<BTCListItem> = ArrayList()

                for (day in result.data.data) {
                    val elem = BTCListItem(
                        Date(day.time * 1000),
                        day.close,
                        currency,
                        day.low,
                        day.high
                    )
                    data.add(elem)
                }
                (viewAdapter as BTCAdapter).setItems(data.reversed() as ArrayList<BTCListItem>)
            }, { error ->
                error.printStackTrace()
            })
    }

    private fun updPreferences() {
        currency = preferences.getString(getString(R.string.currency_key), "USD")!!
        limit = preferences.getString(getString(R.string.limit_key), "10")!!.toIntOrNull()!!
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        updPreferences()
    }
}