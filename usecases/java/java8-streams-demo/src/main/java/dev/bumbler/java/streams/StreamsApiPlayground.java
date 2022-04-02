package dev.bumbler.java.streams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

interface TestSupplier extends Supplier<Optional<Book>> {

}

class Book {

  int id;
  String name;
  double price;
  List<String> authorsList;

  public Book(int id, String name, double price, List<String> authorsList) {
    this.id = id;
    this.name = name;
    this.price = price;
    this.authorsList = authorsList;
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

  public List<String> getAuthorsList() {
    return authorsList;
  }

  public void setAuthorsList(List<String> authorsList) {
    this.authorsList = authorsList;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  @Override
  public String toString() {
    return "Book{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", price=" + price +
        ", authorsList=" + authorsList +
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
        .asList(new Book(1, "Carrasaom", 50, Arrays.asList("Xi Hu", "Riya")),
            new Book(2, "Alphabet", 30, Arrays.asList("Xi Hu", "Ziya")),
            new Book(3, "Zebraa", 10, Arrays.asList("Xi Hu", "Mohan")),
            new Book(4, "Yankees", 60, Arrays.asList("Xi Hu", "Riya")));

    TestConsumer testConsumer = (book) -> System.out.println("book passed: " + book);

    TestPredicate testPredicate = (book) -> book.getName().startsWith("Z");

    TestSupplier testSupplier = () -> Optional.of(
        new Book(11, "testSupplier", 29, Arrays.asList()));

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
    testConsumer.accept(new Book(11, "testConsumer", 32, Arrays.asList()));

    // Testing Predicate Functional Interface
    System.out.println(testPredicate.test(new Book(11, "testPredicate", 22, Arrays.asList())));

    // Testing Supplier Functional Interface
    System.out.println(testSupplier.get().get());

    // Testing forEach function taking Consumer on a Java 8 stream.
    bookList.forEach(testConsumer);

    // Testing filter function taking Predicate on a Java 8 stream.
    bookList.stream().filter(testPredicate).forEach(testConsumer);

    // Testing orElseGet function taking Supplier on a Java 8 stream.
    List<Optional<Book>> emptyList = new ArrayList<>();
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

    // Map operator on Java 8 stream.
    List<List<String>> authorListUsingMap = bookList.stream()
        .map(Book::getAuthorsList)
        .collect(Collectors.toList());
    System.out.println("Author Name List with Map is: " + authorListUsingMap);

    // FlatMap operator on Java 8 stream.
    List<String> authorListUsingFlatMap = bookList.stream()
        .flatMap(book -> book.getAuthorsList().stream())
        .collect(Collectors.toList());
    System.out.println("Author Name List with FlatMap is: " + authorListUsingFlatMap);

    // Using FlatMap and collecting it to a set, so that unique author names appear
    Set<String> authorUniqueListUsingFlatMap = bookList.stream()
        .flatMap(book -> book.getAuthorsList().stream())
        .collect(Collectors.toSet());
    System.out.println("Author Name Unique with using FlatMap is: " + authorUniqueListUsingFlatMap);

    // Optional in Java 8
    Optional<Book> emptyBookOptional_1 = Optional.empty();
    System.out.println(emptyBookOptional_1.orElseGet(
        () -> new Book(1, "Optional Supplier Book", 22, Arrays.asList("Bumbler"))));

    // Optional in Java 8
    Optional<Book> emptyBookOptional_2 = Optional.empty();
    try {
      System.out.println(emptyBookOptional_2.orElseThrow(
          () -> new RuntimeException("Exception for Optional being Empty")));
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
    }

    // Using .reduce() for aggregation in Java 8 streams
    // Calculating the total cart value for all the books in the bookList.
    Double purchasedBooksCartValue = bookList.stream()
        .map(book -> book.getPrice())
        .reduce((aDouble, aDouble2) -> aDouble + aDouble2)
        .get();
    System.out.println("Total Books Cart Value: " + purchasedBooksCartValue);

    // Calculating the costliest book in the bookList
    Double costliestBook = bookList.stream()
        .map(book -> book.getPrice())
        .reduce((aDouble, aDouble2) -> aDouble > aDouble2 ? aDouble : aDouble2)
        .get();
    System.out.println("Costliest Book in Cart is of price: " + costliestBook);

    // Calculate average book price for the book written by 'Riya'
    double riyaBookAveragePrice = bookList.stream()
        .filter(book ->
            book.getAuthorsList().stream().filter(authorName -> authorName.equals("Riya")).count()
                > 0)
        .map(book -> book.getPrice())
        .mapToDouble(price -> price)
        .average().getAsDouble();
    System.out.println("Average Riya Book Price is: " + riyaBookAveragePrice);

    normalStreamExecution(bookList);
    //parallelStreamExecution(bookList);
  }

  /**
   * Normal Streams are sequential. Runs on a single thread, in a single core.
   *
   * @param bookList
   */
  static void normalStreamExecution(List<Book> bookList) {
    long startTime = System.currentTimeMillis();
    IntStream.range(1, 1000)
        .mapToObj(num -> {
          String str = num + " processed by " + Thread.currentThread().getName();
          return str;
        })
        .forEach(System.out::println);
    long endTime = System.currentTimeMillis();
    System.out.println("Total time taken by normal stream: " + (endTime - startTime));
  }

  /**
   * Parallel stream allows us to run the processing and intermediate operations on different
   * threads from different CPU cores. Runs across multiple threads and multiple CPU cores. This
   * helps us to achieve lesser processing time required.
   *
   * @param bookList
   */
  static void parallelStreamExecution(List<Book> bookList) {
    long startTime = System.currentTimeMillis();
    IntStream.range(1, 1000)
        .parallel()
        .mapToObj(num -> {
          String str = num + " processed by " + Thread.currentThread().getName();
          return str;
        })
        .forEach(System.out::println);
    long endTime = System.currentTimeMillis();
    System.out.println("Total time taken by parallel stream: " + (endTime - startTime));
  }
}