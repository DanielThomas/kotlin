trait A {
    fun f(): String
}

inline fun foo(): A {
    return object : A {
        override fun f(): String {
            return "OK"
        }
    }
}

fun box(): String {
    val y = foo()

    val enclosing = y.javaClass.getEnclosingMethod()
    if (enclosing?.getName() != "foo") return "method: $enclosing"

    return y.f()
}
