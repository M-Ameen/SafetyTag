package com.example.safetytag.Model

import android.os.Parcel
import android.os.Parcelable

data class Car(
    var userId: String? = "",
    var carName: String? = "",
    var carColor: String? = "",
    var regNo: String? = "",
    var phoneNo: String? = "",
    var QRCodeImgLink: String? = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userId)
        parcel.writeString(carName)
        parcel.writeString(carColor)
        parcel.writeString(regNo)
        parcel.writeString(phoneNo)
        parcel.writeString(QRCodeImgLink)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Car> {
        override fun createFromParcel(parcel: Parcel): Car {
            return Car(parcel)
        }

        override fun newArray(size: Int): Array<Car?> {
            return arrayOfNulls(size)
        }
    }
}
