/*
 * Copyright 2017 Nathaniel Reline
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
