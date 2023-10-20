import java.lang.reflect.*;
import java.util.Arrays;
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
    private static final LinkedList<Class<?>> recurseQueue = new LinkedList<>();
    private final String EXCLUDE_NAME = "java";
    public void inspect(Object obj, boolean recursive) {

        Class<?> classObj = obj.getClass();
        String className = classObj.getName();

        // Stop once we get to Object level, or if we've already inspected this class
        if (className.startsWith(EXCLUDE_NAME) || seen.contains(className)) {
            System.out.println("This class (" + className + ") has already been seen or is a java class, so it will not be inspected.");
            return;
        }

        // Print details of class with other method
        inspectClass(classObj);

        // Current values of fields (specific to object)
        Field[] fields = classObj.getDeclaredFields();
        if (fields.length == 0) {
            System.out.println("Field Values: None (no fields)");
        } else {
            System.out.println("Field Values:");
            for (Field field : fields) {
                try {
                    field.setAccessible(true);
                    String name = field.getName();
                    Object value = field.get(obj);
                    System.out.print("\t- Field " + name + " = ");
                    if (value == null) {
                        System.out.println("null");
                        continue;
                    }
                    Class<?> valueClass = value.getClass();
                    if (valueClass.isArray()) {
                        printArray(obj, valueClass, recursive);
                    } else if (isWrapperType(valueClass) || valueClass.isPrimitive()) {
                        System.out.println(value);
                    } else {
                        if (!recursive) {
                            System.out.println(value);
                        } else {
                            recurseQueue.add(valueClass);
                            System.out.println(value + ", class details of obj will be printed later.");
                        }
                    }

                } catch (IllegalAccessException | InaccessibleObjectException e) {
                    System.out.println("Could not access this field!");
                }
            }
        }
        System.out.print("\n");
        while (!traversalQueue.isEmpty()) {
            System.out.println("Printing from higher up the hierarchy!");
            inspectClass(traversalQueue.removeLast());
        }

        if (recursive && !recurseQueue.isEmpty()) {
            System.out.println("Printing field objects (since recursive flag was enabled)");
            while (!recurseQueue.isEmpty()) {
                System.out.println("\nPrinting object recursively!");
                inspect(recurseQueue.removeLast(), recursive);
            }
        }

    }

    public void printArray(Object obj, Class<?> objClass, boolean recursive) {
        Class<?> compType = objClass.componentType();
        System.out.println(compType);
        int length = Array.getLength(obj);
        System.out.println(length);
        if (compType.isPrimitive()) {
            for (int i = 0; i < length; i++) System.out.println(Array.get(obj, i));
        } else if (compType.isArray()) {
            Class<?> recursiveClass = Array.get(obj, 0).getClass();
            for (int i = 0; i < length; i++) printArray(Array.get(obj, i), recursiveClass, recursive);
        } else {
            if (recursive) {
                if (!seen.contains(objClass.getName())) recurseQueue.add(objClass);
            } else {
                for (int i = 0; i < length; i++) System.out.println(Array.get(obj, i));
            }
        }

    }

    // From https://stackoverflow.com/questions/709961/determining-if-an-object-is-of-primitive-type
    public static boolean isWrapperType(Class<?> clazz) {
        return clazz.equals(Boolean.class) ||
                clazz.equals(Integer.class) ||
                clazz.equals(Character.class) ||
                clazz.equals(Byte.class) ||
                clazz.equals(Short.class) ||
                clazz.equals(Double.class) ||
                clazz.equals(Long.class) ||
                clazz.equals(Float.class);
    }

    private void inspectClass(Class<?> classObj) {
        String className = classObj.getName();
        if (className.startsWith(EXCLUDE_NAME) || seen.contains(className)) {
            System.out.println("This class (" + className + ") has already been seen or is a java class, so it will not be inspected.");
            return;
        }
        // Name of declaring class
        System.out.println("Declaring Class: " + className);

        // Name of superclass
        Class<?> superClass = classObj.getSuperclass();
        if (superClass != null) {
            String superClassName = superClass.getName();
            if (!seen.contains(superClassName)) traversalQueue.add(superClass);
            System.out.println("Superclass: " + superClassName); // May be "void" (?)
        } else {
            System.out.println("Superclass: None");
        }


        // Name of interface(s)
        Class<?>[] interfaces = classObj.getInterfaces();
        if (interfaces.length == 0) {
            System.out.println("Interfaces: None");
        } else {
            System.out.println("Interfaces:");
            for (Class<?> inf : interfaces) {
                System.out.println("\t- " + inf.getName());
                if (!seen.contains(inf.getName())) traversalQueue.add(inf);
            }
        }

        // Methods
        Method[] methods = classObj.getDeclaredMethods();
        if (methods.length == 0) {
            System.out.println("Methods: None");
        } else {
            System.out.println("Methods:");

            for (Method method : methods) {
                try {
                    method.setAccessible(true);
                    System.out.println("\t- " + method.getName());
                    Class<?>[] exceptions = method.getExceptionTypes();
                    if (exceptions.length == 0) {
                        System.out.println("\t\t- Exceptions Thrown: None");
                    } else {
                        System.out.println("\t\t- Exceptions Thrown:");
                        for (Class<?> exception : exceptions) {
                            System.out.println("\t\t  - " + exception.getName());
                        }
                    }

                    // TODO ensure that the parameter type is easily readable (map types/classes to primitive spelling?)
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
                    System.out.println("\t- Method not accessible");
                }
            }
        }

        // Constructors
        Constructor<?>[] constructors = classObj.getConstructors();
        if (constructors.length == 0) {
            System.out.println("Constructors: None");
        } else {
            try {
                System.out.println("Constructors:");
                for (Constructor<?> constructor : constructors) {
                    constructor.setAccessible(true);

                    // Name
                    System.out.println("- Name: " + constructor.getName());

                    // Parameters
                    Class<?>[] params = constructor.getParameterTypes();
                    if (params.length == 0) {
                        System.out.println("\t- Parameter Types: None");
                    } else {
                        System.out.print("\t- Parameter Types: (");
                        for (int i = 0; i < params.length-2; i++) {
                            System.out.print(params[i].getName() + ", ");
                        }
                        System.out.println(params[params.length-1].getName() + ")");
                    }
                    // Modifiers
                    int mod = constructor.getModifiers();
                    System.out.println("\t- Modifiers: " + Modifier.toString(mod) + "\n");
                }
            } catch (InaccessibleObjectException e) {
                System.out.println("Constructor not accessible");
            }
        }

        // Fields
        Field[] fields = classObj.getDeclaredFields();
        if (fields.length == 0) {
            System.out.println("Fields: None");
        } else {
            System.out.println("Fields: ");
            for (Field field : fields ) {
                try {
                    field.setAccessible(true);
                    System.out.println("\t- " + field.getName());

                    // Field type
                    System.out.print("\t\t- Field Type: ");
                    System.out.println(field.getType().toString());
                    // Modifiers
                    int mod = field.getModifiers();
                    System.out.println("\t\t- Modifiers: " + Modifier.toString(mod));
                } catch (InaccessibleObjectException e) {
                    System.out.println("Field not accessible");
                }
            }
        }

        seen.add(className);
    }
}
