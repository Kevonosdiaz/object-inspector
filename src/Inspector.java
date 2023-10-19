import java.lang.reflect.Constructor;
import java.lang.reflect.InaccessibleObjectException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Inspector class uses reflective techniques to introspect and print
 * details of a given object passed into inspect().
 * Repeated objects will be skipped.
 */
// TODO Do we need to add superclasses into seen?
    // Would be safer in case a superclass instance is present as a field (?)

public class Inspector {
    public Inspector() {}
    private static final HashSet<String> seen = new HashSet<>(); // Store name of already seen classes
    private static final LinkedList<Class<?>> traversalQueue = new LinkedList<>();

    public void inspect(Object obj, boolean recursive) {
        final String EXCLUDE_NAME = "java";
        Class<?> classObj = obj.getClass();
        String className = classObj.getName();

        // Stop once we get to Object level, or if we've already inspected this class
        if (className.startsWith(EXCLUDE_NAME) || seen.contains(className)) return;

        // inspectClass(classObj);
        // Handle current field values and recursion (consider making inspect() a "driver"?)

        // Name of declaring class
        System.out.println("Declaring Class: " + className);

        // Name of superclass
        Class<?> superClass = classObj.getSuperclass();
        String superClassName = superClass.getName();
        if (!seen.contains(superClassName)) traversalQueue.add(superClass);
        System.out.println("Superclass: " + superClassName); // May be "void" (?)

        // Name of interface(s)
        Class<?>[] interfaces = classObj.getInterfaces();
        if (interfaces.length == 0) {
            System.out.println("Interfaces: None");
        } else {
            System.out.println("Interfaces:");
            for (Class<?> inf : interfaces) {
                System.out.println("\t- " + inf.getName());
            }
        }

        // Methods
        Method[] methods = classObj.getDeclaredMethods();
        System.out.println("Methods:");

        // TODO Refactor with Extract Method + consider string builder?
        for (Method method : methods) {
            try {
                method.setAccessible(true);
                // if (method.getName().startsWith(EXCLUDE_NAME)) continue;
                System.out.println("\t- " + method.getName());
                Class<?>[] exceptions = method.getExceptionTypes();
                if (exceptions.length == 0) {
                    System.out.println("\t\t- Exceptions Thrown: None");
                } else {
                    System.out.println("\t\t- Exceptions Thrown:");
                    for (Class<?> exception : exceptions) {
                        System.out.println("\t\t\t- " + exception.getName());
                    }
                }

                // TODO ensure that the parameter type is easily readable (map types/classes to primitive spelling?)
                // TODO reformat so it becomes a comma separated list rather than using spaces
                Class<?>[] params = method.getParameterTypes();
                if (params.length == 0) {
                    System.out.println("\t\t- Parameter Types: None");
                } else {
                    System.out.print("\t\t- Parameter Types: (");
                    for (int i = 0; i < params.length-2; i++) {
                        System.out.print(params[i].getName() + ", ");
                    }
                    System.out.println(params[params.length-1].getName() + ")");
                }

                // Return type
                System.out.print("\t\t- Return Type: ");
                System.out.println(method.getReturnType().getName());
                // Modifiers
                int mod = method.getModifiers();
                System.out.println("\t\t- Modifiers: " + Modifier.toString(mod));
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
            System.out.println("- Name: " + constructor.getName());

            // Parameters
            System.out.print("\t- Parameter Types: ( ");
            Class<?>[] params = constructor.getParameterTypes();
            for (Class<?> param : params) {
                System.out.print(param.getName() + " ");
            }
            System.out.println(")");

            // Modifiers
            int mod = constructor.getModifiers();
            System.out.println("\t- Modifiers: " + Modifier.toString(mod) + "\n");
        }

        // Fields
        // Consider using a separate method for this

        // Current values of fields (specific to object)
        // Check recursive flag, or otherwise just use .toString()
        // Dequeue?


        seen.add(className);
    }

    // Will print all details of a class
    private void inspectClass(Class<?> classObj) {}
}
