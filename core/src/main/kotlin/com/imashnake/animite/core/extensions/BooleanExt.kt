package com.imashnake.animite.core.extensions

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
fun Boolean?.orFalse(): Boolean {
    contract {
        returns(true) implies (this@orFalse != null)
    }
    return this == true
}
