package ie.setu.breakdownassist.firebase

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import ie.setu.breakdownassist.firebase.FirebaseDBManager.database
import ie.setu.breakdownassist.models.BreakdownModel
import ie.setu.breakdownassist.models.BreakdownStore
import timber.log.Timber

object FirebaseDBManager : BreakdownStore {

    var database: DatabaseReference = FirebaseDatabase.getInstance().reference

    override fun findAll(breakdownsList: MutableLiveData<List<BreakdownModel>>) {
        database.child("breakdowns")
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
                    database.child("breakdowns")
                        .removeEventListener(this)

                    breakdownsList.value = localList
                }
            })
    }

    override fun findAll(userid: String, breakdownsList: MutableLiveData<List<BreakdownModel>>) {
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

    val childDelete : MutableMap<String, Any?> = HashMap()
    childDelete["/breakdowns/$breakdownid"] = null
    childDelete["/user-breakdowns/$userid/$breakdownid"] = null

    database.updateChildren(childDelete)
}

override fun update(userid: String, breakdownid: String, breakdown: BreakdownModel) {

    val breakdownValues = breakdown.toMap()

    val childUpdate : MutableMap<String, Any?> = HashMap()
    childUpdate["breakdowns/$breakdownid"] = breakdownValues
    childUpdate["user-breakdowns/$userid/$breakdownid"] = breakdownValues

    database.updateChildren(childUpdate)
}


fun updateImageRef(userid: String,imageUri: String) {

    val userBreakdowns = database.child("user-breakdowns").child(userid)
    val allBreakdowns = database.child("breakdowns")

    userBreakdowns.addListenerForSingleValueEvent(
        object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    //Update Users imageUri
                    it.ref.child("profilepic").setValue(imageUri)
                    //Update all breakdowns that match 'it'
                    val breakdown = it.getValue(BreakdownModel::class.java)
                    allBreakdowns.child(breakdown!!.uid!!)
                        .child("profilepic").setValue(imageUri)
                }
            }
        })
}}