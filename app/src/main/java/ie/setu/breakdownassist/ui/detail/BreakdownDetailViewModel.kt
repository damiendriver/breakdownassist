package ie.setu.breakdownassist.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.setu.breakdownassist.firebase.FirebaseDBManager
import ie.setu.breakdownassist.models.BreakdownModel
import timber.log.Timber

class BreakdownDetailViewModel : ViewModel() {
    private val breakdown = MutableLiveData<BreakdownModel>()

    var observableBreakdown: LiveData<BreakdownModel>
        get() = breakdown
        set(value) {breakdown.value = value.value}

    fun getBreakdown(userid:String, id: String) {
        try {
            FirebaseDBManager.findById(userid, id, breakdown)
            Timber.i("Detail getBreakdown() Success : ${
                breakdown.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("Detail getBreakdown() Error : $e.message")
        }
    }

    fun updateBreakdown(userid:String, id: String,breakdown: BreakdownModel) {
        try {
            //BreakdownManager.update(email, id, breakdown)
            FirebaseDBManager.update(userid, id, breakdown)
            Timber.i("Detail update() Success : $breakdown")
        }
        catch (e: Exception) {
            Timber.i("Detail update() Error : $e.message")
        }
    }
}