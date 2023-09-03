package com.example.githubapp.core


import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

inline fun <reified T : Any, R : Any> handleApiResponse(
    crossinline apiCall: () -> Response<T>,
    crossinline convert: (T) -> R?
): Flow<Resource<R>> {
    return flow {
        emit(Resource.Loading())

        try {
            val response = apiCall()
            if (response.isSuccessful) {
                val responseBody = response.body()
                val convertedResponse = responseBody?.let { convert(it) }
                if (convertedResponse != null) {
                    emit(Resource.Success(convertedResponse))
                } else {
                    emit(Resource.Error(
                        message = "Failed to convert the response body to the desired type."
                    ))
                }
            } else {
                emit(Resource.Error(
                    message = "Response was not successful"
                ))
            }
        } catch (e: HttpException) {
            emit(Resource.Error(
                message = "Oops, something went wrong!"
            ))
        } catch (e: IOException) {
            emit(Resource.Error(
                message = "Couldn't reach the server, check your internet connection."
            ))
        }
    }
}


