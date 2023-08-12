package com.utopiannerd.wordcount;

import java.util.Arrays;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

public class SparkJavaWordCountApp {

  private static final String SAMPLE_FILE_PATH =
      "/mnt/c/Users/anshulgautam/Desktop/sample-input-file.txt";

  public static void main(String[] args) {

    String filePath;
    if (args.length != 0 && args[0] != null) {
      filePath = args[0];
    } else {
      filePath = SAMPLE_FILE_PATH;
    }

    System.out.println("Configured filePath is: " + filePath);

    System.out.println("============Starting wordCount in provided file============");
    wordCount(filePath);
    System.out.println("============Completed wordCount in provided file============");
  }

  /**
   * Count unique English words from the given file. Numbers, punctuations, space, tabs, etc. will
   * not be counted. Also, the words should be case-insensitive, i.e. "Java" and "java" should be
   * counted same.
   *
   * @param filePath - String
   */
  private static void wordCount(String filePath) {

    SparkConf sparkConf = new SparkConf().setMaster("local[2]").setAppName("SparkJavaWordCounter");

    try (JavaSparkContext sparkContext = new JavaSparkContext(sparkConf)) {

      JavaRDD<String> wordsFromFile =
          sparkContext
              .textFile(filePath)
              .map(line -> line.replaceAll("[^a-zA-Z\\s]", "").toLowerCase())
              .flatMap(line -> Arrays.asList(line.split("\\s")).iterator())
              .filter(word -> word != null && word.trim().length() != 0);

      JavaPairRDD<String, Integer> countDataRDD =
          wordsFromFile
              .mapToPair(word -> new Tuple2<>(word, 1))
              .reduceByKey((val1, val2) -> val1 + val2);

      countDataRDD.collect().forEach(System.out::println);
    }
  }
}
