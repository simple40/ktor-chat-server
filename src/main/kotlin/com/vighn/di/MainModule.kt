package com.vighn.di

import com.vighn.Authentication.*
import com.vighn.Data.MessageDataSource
import com.vighn.Data.MessageDataSourceImpl
import com.vighn.room.RoomController
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val mainModule = module {


    single {
        val mongoPW=System.getenv("MONGO_PW")
        val dbName="Chat_db"
        KMongo
            .createClient(
                connectionString = "mongodb+srv://VighneshPandey:$mongoPW@cluster0.cxgaquy.mongodb.net/$dbName?retryWrites=true&w=majority"
            )
            .coroutine
            .getDatabase(dbName)
    }

    single<MessageDataSource> {
        MessageDataSourceImpl( get() )
    }

    single<UserDataSource>{
        UserDateSourceImpl(get())
    }

    single{
        RoomController( get() )
    }



    single<TokenService>{
        JwtTokenService()
    }

    single<HashService>{
        SHA256HashService()
    }


}