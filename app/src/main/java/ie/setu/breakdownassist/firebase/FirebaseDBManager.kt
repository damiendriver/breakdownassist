package ie.setu.breakdownassist.firebase

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import ie.setu.breakdownassist.models.BreakdownModel
import ie.setu.breakdownassist.models.BreakdownStore
import timber.log.Timber

object FirebaseDBManager : BreakdownStore {

    var database: DatabaseReference = FirebaseDatabase.getInstance().reference
    override fun findAll(breakdownsList: MutableLiveData<List<BreakdownModel>>) {
        TODO("Not yet implemented")
    }

    override fun findAll(email: String, userid: String, breakdownsList: MutableLiveData<List<BreakdownModel>>) {

        database.child("user-breakdowns").child(userid)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Timber.i("Firebase Breakdown error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val localList = ArrayList<BreakdownModel>()
                    val children = snapshot.children
                    children.forEach {
                        val breakdown = it.getValue(BreakdownModel::class.java)
                        localList.add(breakdown!!)
                    }
                    database.child("user-breakdowns").child(userid)
                        .removeEventListener(this)

                    breakdownsList.value = localList
                }
            })
    }

    override fun findById(userid: String, breakdownid: String, breakdown: MutableLiveData<BreakdownModel>) {

        database.child("user-breakdowns").child(userid)
            .child(breakdownid).get().addOnSuccessListener {
                breakdown.value = it.getValue(BreakdownModel::class.java)
                Timber.i("firebase Got value ${it.value}")
            }.addOnFailureListener{
                Timber.e("firebase Error getting data $it")
            }
    }
    override fun create(firebaseUser: MutableLiveData<FirebaseUser>, breakdown: BreakdownModel) {
        Timber.i("Firebase DB Reference : $database")

        val uid = firebaseUser.value!!.uid
        val key = database.child("breakdowns").push().key
        if (key == null) {
            Timber.i("Firebase Error : Key Empty")
            return
        }
        breakdown.uid = key
        val breakdownValues = breakdown.toMap()

        val childAdd = HashMap<String, Any>()
        childAdd["/breakdowns/$key"] = breakdownValues
        childAdd["/user-breakdowns/$uid/$key"] = breakdownValues

        database.updateChildren(childAdd)
    }

    override fun delete(userid: String, breakdownid: String) {
        TODO("Not yet implemented")
    }

    override fun update(userid: String, breakdownid: String, breakdown: BreakdownModel) {
        TODO("Not yet implemented")
    }
}