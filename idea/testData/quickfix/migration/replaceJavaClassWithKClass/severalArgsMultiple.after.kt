// "Replace javaClass<T>() with T::class" "true"
// WITH_RUNTIME

Ann(String::class, x = 2, arg = (Int::class), args = array((Any::class), java.lang.String::class)) class MyClass
