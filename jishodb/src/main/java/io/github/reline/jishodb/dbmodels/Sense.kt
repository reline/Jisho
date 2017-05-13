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

import io.github.reline.jishodb.dbmodels.RealmString.Companion.wrap
import io.realm.RealmList
import io.realm.RealmModel
import io.realm.annotations.RealmClass

@RealmClass
open class Sense : RealmModel {

    private var englishDefinitions = RealmList<RealmString>()

    private var partsOfSpeech = RealmList<RealmString>()

    private var links = RealmList<Link>()

    private var tags = RealmList<RealmString>()

    private var restrictions = RealmList<RealmString>()

    private var seeAlso: RealmList<RealmString> = RealmList()

    private var antonyms = RealmList<RealmString>()

    private var source = RealmList<Source>()

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

}
