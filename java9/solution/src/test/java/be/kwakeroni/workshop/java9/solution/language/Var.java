package be.kwakeroni.workshop.java9.solution.language;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

// Examples
// V1 - Replace type declarations with var
// V2 - Inference of generic parameters (to Object)
// V3 - Inference of intersection types
// V4 - var in loops
// V5 - var as variable name
// V6 - var not as class name
// V7 - var not without type inference

// Style guidelines http://openjdk.java.net/projects/amber/LVTIstyle.html
// Principles
// 1. Reading code is more important than writing code.
// 2. Code should be clear from local reasoning.
// 3. Code readability shouldn't depend on IDEs.
// 4. Explicit types are a tradeoff.
// @since 10
public class Var {
    public String var;

    public void test() {
        var var = "var";

        List<String> diamond = new ArrayList<>();
        var stringList = new ArrayList<String>();
        ArrayList<String> sl = stringList;

        var varList = new ArrayList<>();
        ArrayList<Object> ao = varList;

//        Serializable & Comparable<?> x = getSomeX();
        doSomeY(this::getSomeX);
        doSomeX(getSomeX());

        var runnableComparable = getSomeX();
        doSomeY(() -> runnableComparable);

        for (var i=0; i<10; i++){
            // in old loops
        }

        for (var element : varList){
            // or new loops
        }

        var myRunnable = new Runnable() {
            @Override
            public void run() {
                throw new UnsupportedOperationException();
            }

            public void setTimeout(long millis) {
            }

        };

        myRunnable.run();
        myRunnable.setTimeout(1000);

        List<? extends Number> numList = null;
        var num = numList.get(0);
        Number number = num;


        var c = this.getClass();

        var t = getT();


        // 1. Choose variable names that provide useful information.
        // In a var declaration, information about the meaning and use of the variable can be conveyed using the variable's name.
        // Instead of the exact type, it's sometimes better for a variable's name to express the role or the nature of the variable
        // 2. Minimize the scope of local variables.
        // 3. Consider var when the initializer provides sufficient information to the reader.
        // 4. Use var to break up chained or nested expressions with local variables.
        // Using var allows us to express the the code more naturally without paying the high price of explicitly declaring the types of the intermediate variables
        // As with many other situations, the correct use of var might involve both taking something out (explicit types) and adding something back (better variable names, better structuring of code.)
        // 6. Take care when using var with diamond or generic methods.
        // 7. Take care when using var with literals.

    }

    private <T> T getT() {
        return null;
    }

    private <X extends Runnable & Comparable<?>> X getSomeX() {
        return null;
    }
    private <X extends Runnable & Comparable<?>> void doSomeX(X x) {

    }

    private <Y extends Runnable & Comparable<?>> void doSomeY(Supplier<Y> runnableComparable){
        var y = runnableComparable.get();
    }


// Cannot be used as a class name
//    public static final class var {
//
//        private var noFields;
//
//        private var noMethods(var norArguments){
//            try {
//                  var norWithoutInitializer;
//                  var nil = null;
//                  var notWhenTargetTypeIsNeeded = () -> 42;
//                  String[] s1 = {"A", "B"};
//                  var notForArrayInitializers = {"A", "B"};
//            } catch (var orWithCaughtExceptions){
//
//            }
//        }
//    }
}
