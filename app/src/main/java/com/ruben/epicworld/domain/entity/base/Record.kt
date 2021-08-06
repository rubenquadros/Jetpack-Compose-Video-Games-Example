package com.ruben.epicworld.domain.entity.base

/**
 * Created by Ruben Quadros on 01/08/21
 **/
data class Record<out R>(
    val data: R?,
    val error: ErrorRecord?
)
