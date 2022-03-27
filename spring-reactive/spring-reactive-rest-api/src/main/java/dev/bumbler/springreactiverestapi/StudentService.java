package dev.bumbler.springreactiverestapi;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class StudentService {

    public List<Student> getStudentsList() {
        List<Student> fetchedStudentList =
                IntStream.rangeClosed(1, 20)
                        .peek(element -> System.out.println("Fetched student with id: " + element))
                        .peek(StudentService::sleep)
                        .mapToObj(element -> {
                            int rollNumber = element;
                            String name = "Student " + rollNumber;
                            return new Student(rollNumber, name);
                        }).collect(Collectors.toList());
        return fetchedStudentList;
    }

    private static void sleep(int element) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Flux<Student> getStudentsFlux() {
        Flux<Student> fetchedStudentFlux = Flux.range(1, 20)
                .delayElements(Duration.ofSeconds(1))
                .doOnNext(element -> System.out.println("Fetched student with id: " + element))
                .map(element -> {
                    int rollNumber = element;
                    String name = "Student " + rollNumber;
                    return new Student(rollNumber, name);
                });
        return fetchedStudentFlux;
    }
}
