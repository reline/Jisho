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
import io.realm.annotations.RealmClass

@RealmClass
open class Sense : RealmModel {

    private var englishDefinitions = RealmList<RealmString>()

    private var partsOfSpeech = RealmList<RealmString>()

    var links = RealmList<Link>()

    private var tags = RealmList<RealmString>()

    private var restrictions = RealmList<RealmString>()

    private var seeAlso: RealmList<RealmString> = RealmList()

    private var antonyms = RealmList<RealmString>()

    var source = RealmList<Source>()

    private var info = RealmList<RealmString>()

    constructor() {
        // realm constructor
    }

    constructor(definitions: List<String> = emptyList(),
                tags: List<String> = emptyList(),
                information: List<String> = emptyList()) {
        englishDefinitions = wrap(definitions)
        this.tags = wrap(tags)
        info = wrap(information)
    }

    fun getDefinitions() = unwrap(englishDefinitions)

    fun getTags() = unwrap(tags)

    fun getInfo() = unwrap(info)
}
