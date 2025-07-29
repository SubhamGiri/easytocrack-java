
                class Dog {
                    // Fields (attributes)
                    String name;
                    String breed;
                    int age;

                    // Constructor
                    public Dog(String name, String breed, int age) {
                        this.name = name;
                        this.breed = breed;
                        this.age = age;
                    }

                    // Method to simulate barking
                    public void bark() {
                        System.out.println(name + " barks!");
                    }

                    // Method to simulate eating
                    public void eat() {
                        System.out.println(name + " is eating.");
                    }
                }
                public class Main {
                    public static void main(String[] args) {
                        // Creating objects (instances) of the Dog class
                        Dog myDog = new Dog("Buddy", "Golden Retriever", 3); // myDog is an object
                        Dog anotherDog = new Dog("Lucy", "Poodle", 5);     // anotherDog is an object

                        // Accessing object's fields
                        System.out.println(myDog.name + " is a " + myDog.breed + " and is " + myDog.age + " years old.");
                        // Output: Buddy is a Golden Retriever and is 3 years old.

                        // Calling object's methods
                        myDog.bark(); // Output: Buddy barks!
                        anotherDog.eat(); // Output: Lucy is eating.
                    }
                }
                