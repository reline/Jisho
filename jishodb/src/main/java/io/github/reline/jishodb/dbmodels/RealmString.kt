/*
 * Copyright 2017 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package io.github.reline.jishodb.dbmodels

import io.realm.RealmList
import io.realm.RealmModel
import io.realm.annotations.RealmClass
import java.util.*

@RealmClass
open class RealmString : RealmModel {

    lateinit var string: String

    constructor() {
        // realm constructor
    }

    constructor(string: String) {
        this.string = string
    }

    companion object {
        fun wrap(list: List<String>) = list.mapTo(RealmList<RealmString>()) { RealmString(it) }
        fun unwrap(realmList: RealmList<RealmString>) = realmList.mapTo(ArrayList<String>()) { it.string }
    }

}
