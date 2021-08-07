package me.saar.sockets

import me.saar.sockets.reactive.ReplaySubject
import kotlin.concurrent.thread

fun readEvent() = readLine()?.let(ConsoleEvent::parse)

fun consoleEventThread(exitCode: String, onEvent: (ConsoleEvent) -> Unit, onClose: () -> Unit) = thread {
    do {
        val line = readEvent()!!

        onEvent(line)
    } while (line.endpoint != exitCode)

    onClose.invoke()
}

fun consoleEventSubject(exitCode: String): ReplaySubject<ConsoleEvent> {
    val subject = ReplaySubject<ConsoleEvent>()

    consoleEventThread(exitCode, subject::next, subject::close)

    return subject
}