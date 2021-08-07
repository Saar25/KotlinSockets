package me.saar.kouter

class Router<T> : RouteHandler<T> {

    private val routes = mutableListOf<Route<T>>()

    fun route(path: String, handler: RouteHandler<T>): Router<T> {
        this.routes += Route(path, handler)

        return this
    }

    override fun handle(input: RouteInput<T>) {
        val route = this.routes.find { it.match(input.path) }

        route?.handler?.handle(input)
    }
}