package com.vighn.Authentication.Model

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

data class User(
    val username : String,
    val password : String,
    val salt : String,
    @BsonId
    val id : ObjectId= ObjectId()
    )
