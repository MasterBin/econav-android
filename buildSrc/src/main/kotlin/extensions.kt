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