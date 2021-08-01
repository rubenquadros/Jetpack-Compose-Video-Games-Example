package com.ruben.epicworld.remote

import okio.IOException

/**
 * Created by Ruben Quadros on 01/08/21
 **/
sealed class RemoteException(message: String): IOException(message) {
    class ClientError(message: String): RemoteException(message)
    class ServerError(message: String): RemoteException(message)
    class NoNetworkError(message: String): RemoteException(message)
    class GenericError(message: String): RemoteException(message)
}
