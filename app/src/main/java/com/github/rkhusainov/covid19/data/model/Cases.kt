package com.github.rkhusainov.covid19.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Cases(

    @SerializedName("new") val new: Int,
    @SerializedName("active") val active: Int,
    @SerializedName("critical") val critical: Int,
    @SerializedName("recovered") val recovered: Int,
    @SerializedName("total") val total: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(new)
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