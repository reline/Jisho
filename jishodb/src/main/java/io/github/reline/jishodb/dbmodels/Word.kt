/*
 * Copyright 2017 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package io.github.reline.jishodb.dbmodels

import io.github.reline.jishodb.dbmodels.RealmString.Companion.unwrap
import io.github.reline.jishodb.dbmodels.RealmString.Companion.wrap
import io.realm.RealmList
import io.realm.RealmModel
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class Word : RealmModel {
    @PrimaryKey
    var id: Int = 0

    var isCommon: Boolean = false
    var tags: RealmList<RealmString> = RealmList()
    var japanese: RealmList<Japanese> = RealmList()
    var senses: RealmList<Sense> = RealmList()
    var attribution: Attribution? = null

    constructor() {
        // realm constructor
    }

    constructor(id: Int,
                isCommon: Boolean = false,
                tags: List<String> = emptyList(),
                japanese: RealmList<Japanese>,
                senses: RealmList<Sense> = RealmList(),
                attribution: Attribution? = null) {
        this.id = id
        this.isCommon = isCommon
        this.tags = wrap(tags)
        this.japanese = japanese
        this.senses = senses
        this.attribution = attribution
    }

    fun getTags(): List<String> {
        return unwrap(tags)
    }

}
