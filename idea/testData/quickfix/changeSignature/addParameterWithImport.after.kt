// "Add parameter to function 'foo'" "true"
// ERROR: Too many arguments for internal fun foo(): kotlin.Unit defined in b
package a

import b.foo

class Bar

fun test() {
    foo(Bar())
}