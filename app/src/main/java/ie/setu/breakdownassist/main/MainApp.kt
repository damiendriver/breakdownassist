package ie.setu.breakdownassist.main

import android.app.Application
import ie.setu.breakdownassist.models.BreakdownJSONStore
import ie.setu.breakdownassist.models.BreakdownStore
import timber.log.Timber
import timber.log.Timber.Forest.i

class MainApp : Application() {

    lateinit var breakdowns: BreakdownStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        // breakdowns = BreakdownMemStore()
        // breakdowns = BreakdownJSONStore(applicationContext)
        i("Breakdown started")
    }
}