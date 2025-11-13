# The use of enum and why

#### Introduction:

1. represent a fixed set of constants — similar to conventional <mark style="color:$primary;">**`static final`**</mark> constants, but in a type-safe and more readable manner
2. Enum constants: Inside an Enum, we can declare a list of instances

```java
public enum Process {
STARTED,
IN_PROGRESS,
COMPLETED,
FAILED
}

public static void main(String[] args) {
        Process start = Process.STARTED;
        System.out.println(start);
    }
}
```

***

#### Why use Enums ?: <a href="#a1c8" id="a1c8"></a>

* Enums declare a set of values, whereas static final constants are fixed single-value literals.(Readable, **type-safety at compile-time -** the compiler prevents assigning values outside of the predefined set.)

{% hint style="info" %}
Enum declaration can be done outside a class or inside a class but not inside a method. However, enums can access directly.
{% endhint %}

* Enums can be used in switch statements to handle different cases based on the enum constants. Therefore , using enum BookType and Borrower (Student, Staff) to handle different cases based on the enum constants
* Safer switches: switch(BookType) is clearer and less error-prone than switch on chars or strings.
* Visibility and access rules:
  * If enum is public and top-level, any class can use BookType.
  * If nested inside LibrarySystem, declare it public/static (actually nested enums are implicitly static) and main in the same class can refer to BookType directly.
  * Because enums are types, they can be used in static methods (like main) without extra steps.
