package me.saar.kouter

class Router<T> : RouteHandler<T> {

    private val routes = mutableListOf<Route<T>>()

    fun route(path: String, handler: RouteHandler<T>): Router<T> {
        this.routes += Route(path, handler)

        return this
    }

    override fun handle(input: RouteInput<T>) {
        this.routes.find { it.match(input.path) }?.let { route ->
            val path = localPath(input.path, route.path)
            val newInput = RouteInput(path, input.data)
            route.handler.handle(newInput)
        }
    }
}