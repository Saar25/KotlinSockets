package me.saar.sockets.controller

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class BodyField(val name: String = "")