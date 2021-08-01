package com.ruben.epicworld.data.mapper

import android.util.Log
import com.ruben.epicworld.domain.entity.base.ErrorRecord
import com.ruben.epicworld.domain.entity.base.Record
import com.ruben.epicworld.remote.RemoteException

/**
 * Created by Ruben Quadros on 01/08/21
 **/
class ErrorMapper {

    fun <T>mapErrorRecord(e: RemoteException): Record<T> {
        Log.e(ErrorMapper::class.simpleName, e.message.toString())
        val errorRecord: ErrorRecord = when(e) {
            is RemoteException.ClientError -> ErrorRecord.ClientError
            is RemoteException.ServerError -> ErrorRecord.ServerError
            is RemoteException.NoNetworkError -> ErrorRecord.NetworkError
            else -> ErrorRecord.GenericError
        }
        return Record(null, errorRecord)
    }
}