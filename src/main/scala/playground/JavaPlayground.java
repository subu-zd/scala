package playground;

public class JavaPlayground {
  public static void main(String[] args) {
    System.out.println(Person.N_eyes); // Class level functionality: N_eyes can be accessed without relying on an instance of class Person
    
  }
}

class Person {
  public static final int N_eyes = 2; // this is a constant that does not rely on an instance of class Person to access it
}
