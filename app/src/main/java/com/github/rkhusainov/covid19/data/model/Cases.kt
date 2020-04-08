package com.github.rkhusainov.covid19.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Cases(
    @SerializedName("new") val new: String?,
    @SerializedName("active") val active: Int,
    @SerializedName("critical") val critical: Int,
    @SerializedName("recovered") val recovered: Int,
    @SerializedName("total") val total: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    )

    // геттер для поля "new" содержащий только число
    val newIntCase: Int
        get() = new!!.replace("\\D+", "").toInt()

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(new)
        parcel.writeInt(active)
        parcel.writeInt(critical)
        parcel.writeInt(recovered)
        parcel.writeInt(total)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Cases> {
        override fun createFromParcel(parcel: Parcel): Cases {
            return Cases(parcel)
        }

        override fun newArray(size: Int): Array<Cases?> {
            return arrayOfNulls(size)
        }
    }
}