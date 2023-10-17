import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;

public class Inspector {
    private static HashSet<String> seen; // Store name of already seen classes
    public void inspect(Object obj, boolean recursive) {
        // Store all the code that outputs each required piece of information in another method
        // This method should act as a driver, and handle recursive calling (?)
        // Note we can exclude anything with java.* for our assignment
        // Also make modifiers human-readable using Method methods
        // Watch out for repeat recursion (infinite loop)
        // => HashSet of class names (since we are inspecting classes in our case)
        // Check not null (?)
        // => Don't need to check classes which are set to null
        // => Reference the test cases!!!
//        Method[] methods = objectClass.getMethods();
//        System.out.println("\nPublic and Inherited Methods:");
//        System.out.println(Arrays.toString(methods));
//
//        Method[] declaredMethods = objectClass.getDeclaredMethods();
//        System.out.println("\nDeclared Methods:");
//        System.out.println(Arrays.toString(declaredMethods));
//
//        // Working with Constructors
//
//        Constructor[] constructors = objectClass.getConstructors();
//        System.out.println("\nPublic Constructors:");
//        System.out.println(Arrays.toString(constructors));
//
//        Constructor[] declaredConstructors = objectClass.getDeclaredConstructors();
//        System.out.println("\nDeclared Constructors:");
//        System.out.println(Arrays.toString(declaredConstructors));
//
//        // Working with Fields
//
//        Field[] fields = objectClass.getFields();
//        System.out.println("\nPublic and Inherited Fields:");
//        System.out.println(Arrays.toString(fields));
//
//        Field[] declaredFields = objectClass.getDeclaredFields();
//        System.out.println("\nDeclared Fields:");
//        System.out.println(Arrays.toString(declaredFields));
    }
}
