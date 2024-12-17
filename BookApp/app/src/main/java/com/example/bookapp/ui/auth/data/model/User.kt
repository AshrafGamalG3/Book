package com.example.bookapp.ui.auth.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.protobuf.Timestamp

enum class UserType {
    LIBRARIAN,
    NORMAL_USER
}


open class User(
    val timestamp: Long = 0L,
    val type: String = "",
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    var imagePath: String = ""
) : Parcelable {

    constructor() : this(0L, "", "", "", "", "", "")

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
        parcel.writeLong(timestamp)
        parcel.writeString(type)
        parcel.writeString(name)
        parcel.writeString(email)
        parcel.writeString(password)
        parcel.writeString(confirmPassword)
        parcel.writeString(imagePath)
    }

    override fun describeContents(): Int {
        return 0
    }


    fun copy(
        timestamp: Long = this.timestamp,
        type: String = this.type,
        name: String = this.name,
        email: String = this.email,
        password: String = this.password,
        confirmPassword: String = this.confirmPassword,
        imagePath: String = this.imagePath
    ): User {
        return User(timestamp, type, name, email, password, confirmPassword, imagePath)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<User> = object : Parcelable.Creator<User> {
            override fun createFromParcel(parcel: Parcel): User {
                return User(parcel)
            }

            override fun newArray(size: Int): Array<User?> {
                return arrayOfNulls(size)
            }
        }
    }
}
class Librarian : User {

    constructor(
        timestamp: Long,
        name: String,
        email: String,
        password: String,
        confirmPassword: String,
        imagePath: String
    ) : super(timestamp, "Librarian", name, email, password, confirmPassword, imagePath)

    constructor() : super()
}


class NormalUser : User {

    constructor(
        timestamp: Long,
        name: String,
        email: String,
        password: String,
        confirmPassword: String,
        imagePath: String
    ) : super(timestamp, "NormalUser", name, email, password, confirmPassword, imagePath)

    constructor() : super()
}

object UserFactory {
    fun createUser(
        userType: UserType,
        timestamp: Long = System.currentTimeMillis(),
        name: String,
        email: String,
        password: String,
        confirmPassword: String,
        imagePath: String
    ): User {
        return when (userType) {
            UserType.LIBRARIAN -> Librarian(timestamp, name, email, password, confirmPassword, imagePath)
            UserType.NORMAL_USER -> NormalUser(timestamp, name, email, password, confirmPassword, imagePath)
            else -> throw IllegalArgumentException("Unknown user type: $userType") // Add else branch for exhaustive handling
        }
    }
}


