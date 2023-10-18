import java.lang.reflect.InaccessibleObjectException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;

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

        // Name of interfaces
        Class<?>[] interfaces = classObj.getInterfaces();
        System.out.println("Interfaces:\n");
        for (Class<?> inf : interfaces) {
            System.out.println("\t" + inf.getName());
        }
        System.out.println("\n");

        // Methods
        Method[] methods = classObj.getDeclaredMethods();
        System.out.println("Methods:\n");

        for (Method method : methods) {
            try {
                method.setAccessible(true);
                System.out.println("\t‣" + method.getName());
                System.out.println("\t\t‣Exceptions Thrown:");
                Class[] exceptions = method.getExceptionTypes();
                for (Class<?> exception : exceptions) {
                    System.out.println("\t\t\t" + exception.getName());
                }

                System.out.print("\t\t‣Parameter Types: (");
                Class[] params = method.getParameterTypes()
                System.out.println(")");
                // Return type
                System.out.println("\t\t‣Return Type: ");
                System.out.println(method.getReturnType().getName());
                // Modifiers
                int mod = method.getModifiers();
                System.out.println(Modifier.toString(mod));
            } catch (InaccessibleObjectException e) {
                System.out.println("Method not accessible");
            }
            ;
        }

        // Consider using a separate method for this

        // Constructors


        // Fields
        // Consider using a separate method for this


        // Check recursive flag




        seen.add(className);
    }
}
