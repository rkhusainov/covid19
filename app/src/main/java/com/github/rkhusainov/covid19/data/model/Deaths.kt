package com.github.rkhusainov.covid19.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Deaths(
	@SerializedName("new") val new : String?,
	@SerializedName("total") val total : Int
):Parcelable {
	constructor(parcel: Parcel) : this(
		parcel.readString(),
		parcel.readInt()
	)

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeString(new)
		parcel.writeInt(total)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<Deaths> {
		override fun createFromParcel(parcel: Parcel): Deaths {
			return Deaths(parcel)
		}

		override fun newArray(size: Int): Array<Deaths?> {
			return arrayOfNulls(size)
		}
	}
}