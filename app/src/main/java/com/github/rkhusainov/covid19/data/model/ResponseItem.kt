package com.github.rkhusainov.covid19.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class ResponseItem(
    @SerializedName("country") val country: String?,
    @SerializedName("cases") val cases: Cases?,
    @SerializedName("deaths") val deaths: Deaths?,
    @SerializedName("day") val day: String?,
    @SerializedName("time") val time: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readParcelable(Cases::class.java.classLoader),
        parcel.readParcelable(Deaths::class.java.classLoader),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(country)
        parcel.writeParcelable(cases, flags)
        parcel.writeParcelable(deaths, flags)
        parcel.writeString(day)
        parcel.writeString(time)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ResponseItem> {
        override fun createFromParcel(parcel: Parcel): ResponseItem {
            return ResponseItem(parcel)
        }

        override fun newArray(size: Int): Array<ResponseItem?> {
            return arrayOfNulls(size)
        }
    }
}