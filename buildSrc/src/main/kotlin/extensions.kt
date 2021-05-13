import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler

internal fun DependencyHandler.implementation(depName: String) {
    add("implementation", depName)
}

internal fun DependencyHandler.coreLibraryDesugaring(depName: String) {
    add("coreLibraryDesugaring", depName)
}

internal fun DependencyHandler.kapt(depName: String) {
    add("kapt", depName)
}

internal fun DependencyHandler.compileOnly(depName: String) {
    add("compileOnly", depName)
}

internal fun DependencyHandler.api(depName: String) {
    add("api", depName)
}

fun DependencyHandler.implementation(depNames: List<String>) {
    depNames.forEach { dep ->
        add("implementation", dep)
    }
}

fun DependencyHandler.moduleDep(vararg paths: String) {
    moduleDep(paths.toList())
}

fun DependencyHandler.moduleDep(paths: List<String>) {
    paths.forEach {
        add("implementation", project(mapOf("path" to it)))
    }
}

fun DependencyHandler.moduleDepApi(vararg paths: String) {
    moduleDepApi(paths.toList())
}

fun DependencyHandler.moduleDepApi(paths: List<String>) {
    paths.forEach {
        add("api", project(mapOf("path" to it)))
    }
}
