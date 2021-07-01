package com.codingwithmitch.openapi.interactors.blog

import com.codingwithmitch.openapi.api.main.OpenApiMainService
import com.codingwithmitch.openapi.models.AuthToken
import com.codingwithmitch.openapi.models.BlogPost
import com.codingwithmitch.openapi.util.DataState
import com.codingwithmitch.openapi.util.MessageType
import com.codingwithmitch.openapi.util.Response
import com.codingwithmitch.openapi.util.UIComponentType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception

class IsAuthorOfBlogPost(
    private val service: OpenApiMainService,
) {

    fun execute(
        authToken: AuthToken?,
        slug: String,
    ): Flow<DataState<Boolean>> = flow{
        emit(DataState.loading<Boolean>())
        if(authToken == null){
            emit(DataState.error<Boolean>(
                response = Response(
                    message = "Authentication token is invalid. Log out and log back in.",
                    uiComponentType = UIComponentType.Dialog(),
                    messageType = MessageType.Error()
                )
            ))
        }
        try {
            service.isAuthorOfBlogPost(
                "Token ${authToken!!.token!!}",
                slug
            )
        }catch (e: Exception){
            e.printStackTrace()
            emit(DataState.error<Boolean>(
                response = Response(
                    message = e.message,
                    uiComponentType = UIComponentType.Dialog(),
                    messageType = MessageType.Error()
                )
            ))
        }
    }
}



























