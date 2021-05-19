import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler

object Modules {

    object Core {
        const val common = ":core:common"
        const val mapInterface = ":core:map-interface"

        const val network = ":core:network:api"

        const val resources = ":core:resources"
        const val ui = ":core:ui"
    }

    object Data {
        object Routing {
            const val models = ":data:routing:models"
            val api = listOf(":data:routing:api", models)
        }
        object Places {
            const val models = ":data:places:models"
            val api = listOf(":data:places:api", models)
        }
    }

    object Features {
        const val ecoParamElector = ":features:eco-param-elector:api"
        const val general = ":features:general:api"
        const val main = ":features:main:api"
        const val map = ":features:map:api"
        const val routing = ":features:routing:api"
        const val userLocation = ":features:userlocation:api"
        const val searchPlaces = ":features:search-places:api"
        const val placeDetail = ":features:place-details:api"
        const val navigation = ":features:navigation:api"
        const val chooseLocation = ":features:choose-location:api"
    }
}

object ModulesUnsafe {

    const val di = ":di"

    object Core {
        const val networkImpl = ":core:network:impl"
    }

    object Data {
        object Routing {
            const val impl = ":data:routing:impl"
        }
        object Places {
            const val impl = ":data:places:impl"
        }
    }

    object Features {
        const val ecoParamElectorImpl = ":features:eco-param-elector:impl"
        const val generalImpl = ":features:general:impl"
        const val mainImpl = ":features:main:impl"
        const val mapImpl = ":features:map:impl"
        const val routingImpl = ":features:routing:impl"
        const val userLocationImpl = ":features:userlocation:impl"
        const val searchPlaces = ":features:search-places:impl"
        const val placeDetail = ":features:place-details:impl"
        const val navigation = ":features:navigation:impl"
        const val chooseLocation = ":features:choose-location:impl"
    }

}

private fun DependencyHandler.project(vararg paths : String)  {
    paths.forEach {
        add("implementation", projectInner(it))
    }
}

private fun DependencyHandler.projectInner(path: String): Dependency {
    return project(mapOf("path" to path))
}