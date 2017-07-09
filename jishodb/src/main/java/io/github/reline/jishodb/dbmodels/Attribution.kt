/*
 * Copyright 2017 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package io.github.reline.jishodb.dbmodels

import io.realm.RealmModel
import io.realm.annotations.RealmClass

// Issue #14: data is lost when parceled
//    private Object dbpedia; // String or boolean

@RealmClass
open class Attribution : RealmModel {
    var isJmdict: Boolean = false
    var isJmnedict: Boolean = false

    //    public Object isDbpedia() {
    //        return dbpedia;
    //    }

}
