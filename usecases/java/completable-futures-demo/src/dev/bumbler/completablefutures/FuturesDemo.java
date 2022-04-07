package dev.bumbler.completablefutures;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class FuturesDemo {

  public static void main(String[] args) {

    Random random = new Random();

    FutureTask[] futureTaskArray = new FutureTask[5];

    for (int i = 0; i < 5; i++) {
      futureTaskArray[i] = new FutureTask<Integer>(() -> {
        Thread.sleep(2000);
        return random.nextInt(1, 9);
      });
      Thread thread = new Thread(futureTaskArray[i]);
      thread.start();
    }

    for (int i = 0; i < 5; i++) {
      try {
        System.out.println(
            "Output obtained for FutureTask number " + i + " : " + futureTaskArray[i].get());
      } catch (InterruptedException e) {
        e.printStackTrace();
      } catch (ExecutionException e) {
        e.printStackTrace();
      }
    }
  }
}