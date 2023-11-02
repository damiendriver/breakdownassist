package ie.setu.breakdownassist.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BreakdownModel(var id: Long = 0,
                          var title: String = "",
                          var description: String = "",
                          var phone: String = "",
                          var image: Uri = Uri.EMPTY) : Parcelable