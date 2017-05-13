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
