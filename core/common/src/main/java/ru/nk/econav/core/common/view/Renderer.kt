package ru.nk.econav.core.common.view

interface Renderer <in Model : Any> {
    fun render(model : Model)
}

inline fun <Model : Any> render(block: DiffBuilder<Model>.() -> Unit): Renderer<Model> {
    val builder =
            object : DiffBuilder<Model>(), Renderer<Model> {
                override fun render(model: Model) {
                    binders.forEach { it.render(model) }
                }
            }

    builder.block()

    return builder
}

open class DiffBuilder<Model : Any> {

    @PublishedApi
    internal val binders = ArrayList<Renderer<Model>>()

    /**
     * Registers the diff strategy
     *
     * @param get a `getter` to extract a piece of data (typically a field value) from the original `Model`
     * @param compare a `comparator` to compare a new value with the old one, default is `equals`
     * @param set a `consumer` of the values, receives the new value if it is the first value or if the `comparator` returned `false`
     */
    inline fun <T> diff(
            crossinline get: (Model) -> T,
            crossinline compare: (new: T, old: T) -> Boolean = { a, b -> a == b },
            crossinline set: (T) -> Unit
    ) {
        binders +=
                object : Renderer<Model> {
                    private var oldValue: T? = null

                    override fun render(model: Model) {
                        val newValue = get(model)
                        val oldValue = oldValue
                        this.oldValue = newValue

                        if ((oldValue == null) || !compare(newValue, oldValue)) {
                            set(newValue)
                        }
                    }
                }
    }
}