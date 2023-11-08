package com.mobatia.nasmanila.fragments.about_us.model


import java.io.Serializable
import java.util.*

class AboutUsModel: Serializable {
    var Id: String? = null
    var Url: String? = null
    var webUrl: String? = null
    var description: String? = null
    var email: String? = null
    var title: String? = null
    var TabType: String? = null
    var aboutusModelArrayList: ArrayList<AboutUsModel>? = null
    var mFacilitylListArray: ArrayList<AboutUsModel>? = null
    var itemDesc: String? = null
    var itemImageUrl: String? = null
    var itemPdfUrl: String? = null
    var itemTitle: String? = null
}