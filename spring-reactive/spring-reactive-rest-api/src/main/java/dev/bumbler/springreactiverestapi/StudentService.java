package dev.bumbler.springreactiverestapi;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class StudentService {

    public List<Student> getSyncStudents() {
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

    private Flux<Student> getStudentsFlux() {
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

    private static void sleep(int element) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Mono<ServerResponse> getAsyncStudents() {
        return ServerResponse
                .ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(getStudentsFlux(), Student.class);
    }
}
