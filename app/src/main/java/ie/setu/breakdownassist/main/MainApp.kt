package ie.setu.breakdownassist.main

import android.app.Application
import ie.setu.breakdownassist.models.BreakdownModel
import timber.log.Timber
import timber.log.Timber.Forest.i

class MainApp : Application() {

    val breakdowns = ArrayList<BreakdownModel>()
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        i("BreakdownAssist started")
    }
}