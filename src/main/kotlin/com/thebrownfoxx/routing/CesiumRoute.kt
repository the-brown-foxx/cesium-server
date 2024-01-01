package com.thebrownfoxx.routing
//
//import com.thebrownfoxx.auth.AdminService
//import com.thebrownfoxx.auth.models.JWTConfig
//import com.thebrownfoxx.totp.AccessorService
//import io.ktor.server.application.*
//import io.ktor.server.routing.*
//
//open class CesiumRoute(
//    val jwtConfig: JWTConfig,
//    val adminService: AdminService,
//    val accessorService: AccessorService,
//    parent: Route?,
//    selector: RouteSelector,
//) : Route(parent, selector)
//
//class CesiumRouting(
//    jwtConfig: JWTConfig,
//    adminService: AdminService,
//    accessorService: AccessorService,
//    val application: Application,
//) : CesiumRoute(
//    jwtConfig = jwtConfig,
//    adminService = adminService,
//    accessorService = accessorService,
//    parent = null,
//    selector = RootRouteSelector(application.environment.rootPath),
//)
//
//fun Application.cesiumRouting(
//    jwtConfig: JWTConfig,
//    adminService: AdminService,
//    accessorService: AccessorService,
//    configuration: CesiumRouting.() -> Unit,
//): Routing {
//    val route = CesiumRouting(
//        jwtConfig = jwtConfig,
//        adminService = adminService,
//        accessorService = accessorService,
//        application = this,
//    )
//    return pluginOrNull(Routing).apply { route.configuration() } ?: install(Routing) {
//        route.configuration()
//    }
//}
