# Part 2: Gradle Build Tool

## Step 1

- 5: 9
- 6: app.jar
- 9: There are 3 files in the directory. The app entry point is still app.jar. This diference in the ammount of files inside lib/ is due to we not including lib guava. The missing libraries are lib guava and its dependencies.

## Step 2

- 10: The folder contains 4 files. The app.jar file belongs to the App module and the demo.jar belongs to the Demo module. The names are: "annotations-13.0.jar", "app.jar", "demo.jar", "kotlin-stdlib-2.2.21.jar"
- 11: The outuput seen was "Hello from demo module, World!". Graddle built the app module, including the already built demo module, in the classpath. 
- 12: app.jar only contains AppKt.class. demo.jar only contains Greeter.class.
