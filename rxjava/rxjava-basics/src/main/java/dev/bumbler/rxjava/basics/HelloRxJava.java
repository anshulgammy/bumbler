package dev.bumbler.rxjava.basics;

import io.reactivex.Observable;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.schedulers.Schedulers;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HelloRxJava {

    public static void main(String[] args) {
        observableFromJust();
        observableFromIterable();
        observableFromCreate();
        observableFromRange();
        observableFromRangeWithThread();
        observableWithErrorHandlingAtSubscribeLevel();
        observableWithErrorHandlingAtOperatorLevel();
        observableWithRetry();
        hotObservableExample();
        hotObservableChangedOrderExample();
    }

    /**
     * Creating Observable from just, and subscribing to it.
     */
    private static void observableFromJust() {
        System.out.println("observableFromJust::Example---------->");
        Observable.just(1, 2, 3, 4, 5)
                .subscribe(HelloRxJava::print);
    }

    /**
     * Creating Observable from Iterable and subscribing to it.
     */
    private static void observableFromIterable() {
        System.out.println("observableFromIterable::Example---------->");
        List<Integer> integerList = Arrays.asList(1, 2, 3, 4, 5);
        Observable<Integer> observable = Observable.fromIterable(integerList);
        observable.subscribe(HelloRxJava::print);
    }

    /**
     * Creating Observable from ObservableOnSubscribe<T> using create, and subscribing to it.
     */
    private static void observableFromCreate() {
        System.out.println("observableFromCreate::Example---------->");
        Observable<Integer> observable = Observable.create(observableEmitter -> {
            observableEmitter.onNext(1);
            observableEmitter.onNext(2);
            observableEmitter.onNext(3);
            observableEmitter.onNext(4);
            observableEmitter.onNext(5);
        });

        observable
                // filtering the element on the basis of odd and even. Only even numbers will be considered.
                .filter(element -> element % 2 == 0)
                // multiplying the element with 10
                .map(element -> element * 10)
                .subscribe(HelloRxJava::print);
    }

    /**
     * Creating Observable from ObservableOnSubscribe<T> using range, and subscribing to it.
     * Also employed filter() and map() Operators.
     */
    private static void observableFromRange() {
        System.out.println("observableFromRange::Example---------->");
        Observable<Integer> observable = Observable.range(1, 10);

        observable
                // filtering the element on the basis of odd and even. Only even numbers will be considered.
                .filter(element -> element % 2 == 0)
                // multiplying the element with 10
                .map(element -> element * 10)
                .subscribe(HelloRxJava::print);
    }

    /**
     * Demonstrating switching of threads by Scheduler
     */
    private static void observableFromRangeWithThread() {
        System.out.println("observableFromRangeWithThread::Example---------->");

        Observable<Integer> observable = Observable.range(1, 10);

        // odd numbers will be processed on IO thread
        observable.range(1, 10)
                .filter(element -> element % 2 == 0)
                .map(element -> element * 10)
                .observeOn(Schedulers.io())
                .subscribe(element -> {
                    System.out.println(element);
                    System.out.println(Thread.currentThread().getName());
                });

        // odd numbers will be processed on Computation thread
        observable
                .filter(element -> element % 2 == 1)
                .observeOn(Schedulers.computation())
                .map(element -> element * 10)
                .subscribe(element -> {
                    System.out.println(Thread.currentThread().getName());
                    print(element);
                });

        // This is important to add a thread sleep below.
        // Otherwise, the Java main thread will end as soon as the execution passes .subscribe() above.
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Demonstrating error handling at Subscribe level
     */
    private static void observableWithErrorHandlingAtSubscribeLevel() {
        System.out.println("observableWithErrorHandlingAtSubscribeLevel::Example---------->");

        // Observable doesn't allow to emmit null. I have added that on purpose so that Exception gets raised.
        Observable<Integer> observable = Observable.create(emitter -> {
            emitter.onNext(1);
            emitter.onNext(2);
            emitter.onNext(3);
            emitter.onNext(4);
            emitter.onNext(5);
            emitter.onNext(null);
        });

        observable
                // filtering the element on the basis of odd and even. Only even numbers will be considered.
                .filter(element -> element % 2 == 0)
                // multiplying the element with 10
                .map(element -> element * 10)
                .subscribe(System.out::println, (error) -> {
                    System.out.println("Error happened: " + error.getMessage());
                });
    }

    /**
     * Demonstrating error handling at Operator level
     */
    private static void observableWithErrorHandlingAtOperatorLevel() {
        System.out.println("observableWithErrorHandlingAtOperatorLevel::Example---------->");

        // Observable doesn't allow to emmit null. I have added that on purpose so that Exception gets raised.
        Observable<Integer> observable = Observable.create(emitter -> {
            emitter.onNext(1);
            emitter.onNext(2);
            emitter.onNext(3);
            emitter.onNext(4);
            emitter.onNext(5);
            emitter.onNext(null);
        });

        observable
                // filtering the element on the basis of odd and even. Only even numbers will be considered.
                .filter(element -> element % 2 == 0)
                // multiplying the element with 10
                .map(element -> element * 10)
                .onErrorReturnItem(-1)
                .subscribe(System.out::println);
    }

    /**
     * Demonstrating error handling with retry at Subscribe level
     */
    private static void observableWithRetry() {
        System.out.println("observableWithErrorHandlingAndRetryAtSubscribeLevel::Example---------->");

        // Observable doesn't allow to emmit null. I have added that on purpose so that Exception gets raised.
        Observable<Integer> observable = Observable.create(emitter -> {
            emitter.onNext(1);
            emitter.onNext(2);
            emitter.onNext(3);
            emitter.onNext(4);
            emitter.onNext(5);
            emitter.onNext(null);
        });

        observable
                // filtering the element on the basis of odd and even. Only even numbers will be considered.
                .filter(element -> element % 2 == 0)
                // multiplying the element with 10
                .map(element -> element * 10)
                .retryWhen(element -> {
                    return element.take(10).delay(2, TimeUnit.SECONDS);
                })
                .subscribe(System.out::println);

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void hotObservableExample() {

        ConnectableObservable<Integer> observable = Observable.range(1, 3).publish();

        observable.subscribe(element -> {
            System.out.println("Observer 1 printing: " + element);
        });

        observable.subscribe(element -> {
            System.out.println("Observer 2 printing: " + element);
        });

        observable.connect();
    }

    private static void hotObservableChangedOrderExample() {

        ConnectableObservable<Integer> observable = Observable.range(1, 3).publish();

        observable.subscribe(element -> {
            System.out.println("Observer 1 printing: " + element);
        });

        observable.connect();

        observable.subscribe(element -> {
            System.out.println("Observer 2 printing: " + element);
        });
    }

    private static void print(Integer element) {
        System.out.println("Printing number: " + element);
    }
}
