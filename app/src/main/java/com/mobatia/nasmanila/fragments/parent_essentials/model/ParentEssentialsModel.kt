package com.mobatia.nasmanila.fragments.parent_essentials.model

import java.io.Serializable

class ParentEssentialsModel:Serializable {
    var newsLetterCatId: String? = null
    var newsLetterCatName: String? = null
    var newsLetterModelArrayList: ArrayList<ParentEssentialsModel>? = null
    var title: String? = null
    var newLetterSubId: String? = null
    var filename: String? = null
    var submenu: String? = null
    var bannerImage: String? = null
    var link: String? = null
    var description: String? = null
    var contactEmail: String? = null
}