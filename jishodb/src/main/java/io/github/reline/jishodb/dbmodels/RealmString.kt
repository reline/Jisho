package io.github.reline.jishodb.dbmodels

import io.realm.RealmModel
import io.realm.annotations.RealmClass

@RealmClass
open class RealmString : RealmModel {

    lateinit var string: String

    constructor() {
        // realm constructor
    }

    constructor(string: String) {
        this.string = string
    }

}
