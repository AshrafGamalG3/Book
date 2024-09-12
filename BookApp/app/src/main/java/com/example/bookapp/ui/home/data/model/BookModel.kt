package com.example.bookapp.ui.home.data.model

import android.os.Parcel
import android.os.Parcelable

data class BookModel(
    var favorite : Boolean,
    var bookViews:Int,
    var bookDownloads:Int,
    val categoryName: String,
    val description: String,
    val id: String,
    val pdfUrl: String,
    val title: String,
    val userId: String,
    val timestamp: Long,
) : Parcelable {
    constructor() : this(false,0,0,"", "", "", "", "", "", 0)

    constructor(parcel: Parcel) : this(
        parcel.readByte() != 0.toByte(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readLong()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(categoryName)
        parcel.writeString(description)
        parcel.writeString(id)
        parcel.writeString(pdfUrl)
        parcel.writeString(title)
        parcel.writeString(userId)
        parcel.writeLong(timestamp)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<BookModel> {
        override fun createFromParcel(parcel: Parcel): BookModel {
            return BookModel(parcel)
        }

        override fun newArray(size: Int): Array<BookModel?> {
            return arrayOfNulls(size)
        }
    }
}
