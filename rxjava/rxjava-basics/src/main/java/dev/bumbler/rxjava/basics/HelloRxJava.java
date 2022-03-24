package dev.bumbler.rxjava.basics;

import io.reactivex.Observable;

import java.util.Arrays;
import java.util.List;

public class HelloRxJava {

    public static void main(String[] args) {
        observableFromJust();
        observableFromIterable();
        observableFromCreate();
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
        observable.subscribe(HelloRxJava::print);
    }

    private static void print(Integer element) {
        System.out.println("Printing number: " + element);
    }
}
