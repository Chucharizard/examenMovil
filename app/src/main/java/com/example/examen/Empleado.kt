package com.example.examen

import android.os.Parcel
import android.os.Parcelable

data class Empleado(
    val nombre: String,
    val apellido: String,
    val usuario: String,
    val contrasena: String,
    val email: String,
    val telefono: String,
    val imagenPath: String
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nombre)
        parcel.writeString(apellido)
        parcel.writeString(usuario)
        parcel.writeString(contrasena)
        parcel.writeString(email)
        parcel.writeString(telefono)
        parcel.writeString(imagenPath)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Empleado> {
        override fun createFromParcel(parcel: Parcel): Empleado {
            return Empleado(parcel)
        }

        override fun newArray(size: Int): Array<Empleado?> {
            return arrayOfNulls(size)
        }
    }
}
