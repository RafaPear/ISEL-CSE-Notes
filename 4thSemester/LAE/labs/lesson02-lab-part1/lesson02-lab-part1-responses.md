# Lesson 2 Lab 1

## Step 1

- 1: Where generated 3 `.class` files. As each type is identified by a `.class` file, the class X, interface Y and class Z, each have their own `.class` file.
- 2: 1 `.class` file was generated. As the app is the only type defined in the `App.java` file, it is also the only `.class` generated.
- 3: There were no errors when compiling the file `App.java`. As the interface Y is never used, it also is never searched by the compiler.
- 4: There was an "cannot find symbol" error for the class Z. This error happens because the class Z is referenced inside `App.java`. Therefore the compiler will search for it.
- 5: There were no errors. The JVM does delayed loading. This means that it will only load a `.class` when it needs to be used. As the types defined by Y.class and Z.class were never needed when running App, they will never be loaded.
- 6: The program errors out after reading the line, when it tries to instanciate the class X. As the `.class` files are loaded in runtime (dynamicly) it only errors out when it actually tries to find the `X.class` im the classpath
- 7: When running App the first time, it showed "I am X". After modifying the class X in Foo.java and recompiling, the app shows "I am X version 2". This happens because the `.class` files are loaded in runtime when needed. In the second time running app, the X.class file was diferent, but as it still refers to the class X.

## Step 2

- 1: There was an error. The kotlin compiler could not find X.class. This hapened because the kotlin compiler also tries to find the corresponding `.class` files matching the types used in the program, but not inside the current path. Differently from the java compiler, the kotlin compiler does not try to find `.class` files in the current folder.
- 2: It generated AppKt.class
- 3: Kotlin implicitly defines its own libraries in the classpath. Java does also do that but with its own libraries. This means that Java does not know about the kotlin libraries as they were not in the classpath. Thats why the program errors out when running with java and not with kotlin.
- 4: Kotlin auto defines its libraries in the classpath. Java does not define kotlin libraries in the classpath, except if we tell it to do so. If we tell java were the needed libraries are, it will add them to the classpath and the error will disapear. 
