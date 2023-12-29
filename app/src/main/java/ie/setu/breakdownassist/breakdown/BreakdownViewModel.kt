package ie.setu.breakdownassist.breakdown

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.setu.breakdownassist.firebase.FirebaseDBManager
import ie.setu.breakdownassist.firebase.FirebaseImageManager
import ie.setu.breakdownassist.models.BreakdownModel

class BreakdownViewModel : ViewModel(){
    private val status = MutableLiveData<Boolean>()

    val observableStatus: LiveData<Boolean>
        get() = status

    fun addBreakdown(firebaseUser: MutableLiveData<FirebaseUser>,
                    breakdown: BreakdownModel) {
        status.value = try {
            //BreakdownManager.create(breakdown)
            breakdown.profilepic = FirebaseImageManager.imageUri.value.toString()
            FirebaseDBManager.create(firebaseUser,breakdown)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }
}
