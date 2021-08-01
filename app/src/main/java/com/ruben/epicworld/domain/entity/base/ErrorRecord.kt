package com.ruben.epicworld.domain.entity.base

/**
 * Created by Ruben Quadros on 01/08/21
 **/
sealed class ErrorRecord {
    object ClientError: ErrorRecord()
    object ServerError: ErrorRecord()
    object NetworkError: ErrorRecord()
    object GenericError: ErrorRecord()
}
