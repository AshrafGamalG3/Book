package com.example.bookapp.ui.auth.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.protobuf.Timestamp

data class User(
    val timestamp: Long,
    val type: String,
    val name: String,
    val email: String,
    val password: String,
    val confirmPassword: String,
    var imagePath: String
) : Parcelable {
    constructor() : this(0,"", "", "", "", "", "")

    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(type)
        parcel.writeString(name)
        parcel.writeString(email)
        parcel.writeString(password)
        parcel.writeString(confirmPassword)
        parcel.writeString(imagePath)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}
