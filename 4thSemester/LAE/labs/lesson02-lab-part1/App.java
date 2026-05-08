// App.java
public class App {
    public static void main(String[] args) {
        System.out.println("Press ENTER to proceed.");
        System.console().readLine();
        new X().print();
    }
    public static void bar() {
        new Z().print();
    }
}
