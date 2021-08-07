package me.saar.sockets.controller

import me.saar.kouter.controller.Controller
import me.saar.kouter.controller.buildRouter
import me.saar.sockets.ConsoleEvent

fun Controller.buildSocketRouter() = buildRouter(socketArgProviders)

fun Controller.buildConsoleRouter() = buildRouter<ConsoleEvent>()