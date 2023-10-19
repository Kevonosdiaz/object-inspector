import java.lang.reflect.Constructor;
import java.lang.reflect.InaccessibleObjectException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;

/**
 * Goal of the program
 */

public class Inspector {
    private static final HashSet<String> seen = new HashSet<>(); // Store name of already seen classes
    public void inspect(Object obj, boolean recursive) {
        Class<?> classObj = obj.getClass();
        String className = classObj.getName();
        // Stop once we get to Object level, or if we've already inspected this class
        if (className.startsWith("java.lang") || seen.contains(className)) return;

        // Name of declaring class
        System.out.println("Declaring Class:\t" + className);

        // Name of superclass
        Class<?> superClass = classObj.getSuperclass();
        String superClassName = superClass.getName();
        System.out.println("Superclass:\t" + superClassName); // May be "void" (?)

        // Name of interface(s)
        Class<?>[] interfaces = classObj.getInterfaces();
        System.out.println("Interfaces:\n");
        for (Class<?> inf : interfaces) {
            System.out.println("\t" + inf.getName());
        }
        System.out.print("\n");

        // Methods
        Method[] methods = classObj.getDeclaredMethods();
        System.out.println("Methods:");

        // TODO Refactor with Extract Method + consider string builder?
        for (Method method : methods) {
            try {
                method.setAccessible(true);
                System.out.println("\t‣" + method.getName());
                System.out.println("\t\t‣Exceptions Thrown:");
                Class<?>[] exceptions = method.getExceptionTypes();
                for (Class<?> exception : exceptions) {
                    System.out.println("\t\t\t" + exception.getName());
                }

                // TODO ensure that the parameter type is easily readable (map types/classes to primitive spelling?)

                System.out.print("\t\t‣Parameter Types: ( ");
                Class<?>[] params = method.getParameterTypes();
                for (Class<?> param : params) {
                    System.out.print(param.getName() + " ");
                }
                System.out.println(")");

                // Return type
                System.out.print("\t\t‣Return Type: ");
                System.out.println(method.getReturnType().getName());
                // Modifiers
                int mod = method.getModifiers();
                System.out.println("\t‣Modifiers: " + Modifier.toString(mod));
            } catch (InaccessibleObjectException e) {
                System.out.println("Method not accessible");
            }
        }

        // TODO add in try/catch block, consider Extracting Method if too long
        // Constructors
        Constructor<?>[] constructors = classObj.getConstructors();
        System.out.println("Constructors:");
        for (Constructor<?> constructor : constructors) {
            // Name
            System.out.println("\t‣Name: " + constructor.getName());

            // Parameters
            System.out.print("\t‣Parameter Types: ( ");
            Class<?>[] params = constructor.getParameterTypes();
            for (Class<?> param : params) {
                System.out.print(param.getName() + " ");
            }
            System.out.println(")");

            // Modifiers
            int mod = constructor.getModifiers();
            System.out.println("\t‣Modifiers: " + Modifier.toString(mod));
        }

        // Fields
        // Consider using a separate method for this


        // Check recursive flag, or otherwise just use .toString()
        // Dequeue?


        seen.add(className);
    }

    private Class<?> inspectClass(Class<?> classObj) {

    }
}
