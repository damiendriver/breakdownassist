package ie.setu.breakdownassist.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.setu.breakdownassist.firebase.FirebaseDBManager
import ie.setu.breakdownassist.models.BreakdownModel
import timber.log.Timber
import java.lang.Exception

class ListViewModel : ViewModel() {

    private val breakdownsList =
        MutableLiveData<List<BreakdownModel>>()
    var readOnly = MutableLiveData(false)

    val observableBreakdownsList: LiveData<List<BreakdownModel>>
        get() = breakdownsList

    var liveFirebaseUser = MutableLiveData<FirebaseUser>()

    init { load() }

    fun load() {
        try {
            readOnly.value = false
            //BreakdownManager.findAll(liveFirebaseUser.value?.email!!, breakdownsList)
            FirebaseDBManager.findAll(liveFirebaseUser.value?.uid!!,breakdownsList)
            Timber.i("Report Load Success : ${breakdownsList.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("Report Load Error : $e.message")
        }
    }

    fun loadAll() {
        try {
            readOnly.value = true
            FirebaseDBManager.findAll(breakdownsList)
            Timber.i("Report LoadAll Success : ${breakdownsList.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("Report LoadAll Error : $e.message")
        }
    }

    fun delete(userid: String, id: String) {
        try {
            //BreakdownManager.delete(userid,id)
            FirebaseDBManager.delete(userid,id)
            Timber.i("Report Delete Success")
        }
        catch (e: Exception) {
            Timber.i("Report Delete Error : $e.message")
        }
    }
}
