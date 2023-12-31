package ie.setu.breakdownassist.models

import android.net.Uri
import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize

/*
@Parcelize
data class BreakdownModel(var id: Long = 0,
                          var title: String = "",
                          var description: String = "",
                          var phone: String = "",
                          var image: Uri = Uri.EMPTY,
                          var lat : Double = 0.0,
                          var lng: Double = 0.0,
                          var zoom: Float = 0f) : Parcelable

@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable

*/

@IgnoreExtraProperties
@Parcelize
data class BreakdownModel(
    var uid: String? = "",
    var title: String = "Test",
    var open: String = "Test1",
    var type: String = "Test2",
    var description: String = "Test3",
    var profilepic: String = "",
    var email: String? = "joe@bloggs.com") : Parcelable
{
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "title" to title,
            "open" to open,
            "type" to type,
            "description" to description,
            "profilepic" to profilepic,
            "email" to email
        )
    }
}