public class Testing {
    public static void test() {
        mockLib.foo.LibClass.<caret>
    }
}

// EXIST: Companion
// EXIST: OBJECT$