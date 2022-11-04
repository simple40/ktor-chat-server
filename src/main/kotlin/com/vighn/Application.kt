package com.vighn

import com.vighn.Authentication.TokenConfig
import com.vighn.di.mainModule
import io.ktor.server.application.*
import com.vighn.plugins.*
import org.koin.ktor.plugin.Koin

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    install(Koin){
        modules(mainModule)
    }

    val config = TokenConfig(
        secret =environment.config.property("jwt.secret").getString(),
        issuer =environment.config.property("jwt.issuer").getString(),
        audience =environment.config.property("jwt.audience").getString(),
        realm =environment.config.property("jwt.realm").getString(),
        expireIN = 30L * 24L * 60L * 60L * 1000L
    )
    configureSockets()
    configureSerialization()
    configureMonitoring()
    configureSecurity()
    configureRouting(config)
    configureSecurityJWT(config)
}
