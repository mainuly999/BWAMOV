package com.ainul.belajarmov.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Film (
    var desc: String? = "",
    var director: String? = "",
    var genre: String? = "",
    var judul: String? = "",
    var poster: String? = "",
    var rating: String? = ""
): Parcelable