package ie.setu.breakdownassist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import timber.log.Timber
import timber.log.Timber.Forest.i

class BreakdownActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_breakdown)

        Timber.plant(Timber.DebugTree())
        i("Breakdown Assist has started..")
    }
}