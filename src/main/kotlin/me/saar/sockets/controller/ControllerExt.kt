package me.saar.sockets.controller

import com.github.saar25.kouter.controller.Controller
import com.github.saar25.kouter.controller.buildRouter
import me.saar.sockets.ConsoleEvent

fun Controller.buildSocketRouter() = buildRouter(socketArgProviders)

fun Controller.buildConsoleRouter() = buildRouter<ConsoleEvent>()