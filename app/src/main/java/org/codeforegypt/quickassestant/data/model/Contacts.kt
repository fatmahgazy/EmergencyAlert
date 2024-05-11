package org.codeforegypt.quickassestant.data.model

import com.google.gson.annotations.SerializedName

data class Contacts(
    @SerializedName("phones")
    val contacts: List<Contact>
)
