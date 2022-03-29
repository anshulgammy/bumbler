package dev.bumbler.java.streams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@FunctionalInterface
interface Calculator {

  void calculate();
}

@FunctionalInterface
interface Add {

  int add(int num1, int num2);
}

interface TestConsumer extends Consumer<Book> {

}

interface TestPredicate extends Predicate<Book> {

}

interface TestSupplier extends Supplier<Book> {

}

class Book {

  int id;
  String name;

  public Book(int id, String name) {
    this.id = id;
    this.name = name;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "Book{" +
        "id=" + id +
        ", name='" + name + '\'' +
        '}';
  }
}

public class StreamsApiPlayground {

  public static void main(String[] args) {
    Calculator calculator = () -> {
      System.out.println("Calculating...");
    };

    Add add = (num1, num2) -> num1 + num2;

    List<Book> bookList = Arrays
        .asList(new Book(1, "Carrasaom"),
            new Book(2, "Alphabet"),
            new Book(3, "Zebraa"),
            new Book(4, "Yankees"));

    TestConsumer testConsumer = (book) -> System.out.println("book passed: " + book);

    TestPredicate testPredicate = (book) -> book.getName().startsWith("Z");

    TestSupplier testSupplier = () -> new Book(11, "testSupplier");

    // Creating map to iterate through it using forEach.
    Map<Integer, Book> booksMap = bookList.stream()
        .collect(Collectors.toMap(Book::getId, Function.identity()));

    // Execution code starts.
    calculator.calculate();
    System.out.println("Addition Result: " + add.add(10, 12));

    // Sorting in ascending order
    bookList.sort((book1, book2) -> book1.getName().compareTo(book2.getName()));
    System.out.println(bookList);

    // Sorting in descending order
    bookList.sort((book1, book2) -> book2.getName().compareTo(book1.getName()));
    System.out.println(bookList);

    // Testing Consumer Functional Interface
    testConsumer.accept(new Book(11, "testConsumer"));

    // Testing Predicate Functional Interface
    System.out.println(testPredicate.test(new Book(11, "testPredicate")));

    // Testing Supplier Functional Interface
    System.out.println(testSupplier.get());

    // Testing forEach function taking Consumer on a Java 8 stream.
    bookList.forEach(testConsumer);

    // Testing filter function taking Predicate on a Java 8 stream.
    bookList.stream().filter(testPredicate).forEach(testConsumer);

    // Testing orElseGet function taking Supplier on a Java 8 stream.
    List<Book> emptyList = new ArrayList<>();
    System.out.println(emptyList.stream().findAny().orElseGet(testSupplier));

    // Iterating through the map using Java 8 stream.
    booksMap.entrySet().stream().forEach(element -> {
      System.out.println("Key: " + element.getKey() + " Value: " + element.getValue());
    });

    // Sorting the map through Java 8 stream. Approach 1
    booksMap.entrySet().stream()
        .sorted(
            (entry1, entry2) -> entry1.getValue().getName().compareTo(entry2.getValue().getName()))
        .forEach(System.out::println);

    // Sorting the map through Java 8 stream. Approach 2
    booksMap.entrySet().stream()
        .sorted(Map.Entry.comparingByValue(Comparator.comparing(Book::getName).reversed()))
        .forEach(System.out::println);
    // Execution code ends.
  }
}