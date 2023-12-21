package ie.setu.breakdownassist.models

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser


interface BreakdownStore {
    fun findAll(): List<BreakdownModel>
    fun create(breakdown: BreakdownModel)
    fun update(breakdown: BreakdownModel)
    fun delete(breakdown: BreakdownModel)
    fun findById(id:Long) : BreakdownModel?
}
/*
interface BreakdownStore {
    fun findAll(breakdownsList:
                MutableLiveData<List<BreakdownModel>>
    )
    fun findAll(userid:String,
                breakdownsList:
                MutableLiveData<List<BreakdownModel>>)
    fun findById(userid:String, breakdownid: String,
                 breakdown: MutableLiveData<BreakdownModel>)
    fun create(firebaseUser: MutableLiveData<FirebaseUser>, breakdown: BreakdownModel)
    fun delete(userid:String, breakdownid: String)
    fun update(userid:String, breakdownid: String, breakdown: BreakdownModel)
}
*/