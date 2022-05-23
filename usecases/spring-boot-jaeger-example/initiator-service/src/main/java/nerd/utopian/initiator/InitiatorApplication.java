package nerd.utopian.initiator;

import io.jaegertracing.Configuration;
import io.jaegertracing.Configuration.ReporterConfiguration;
import io.jaegertracing.Configuration.SamplerConfiguration;
import io.jaegertracing.internal.JaegerTracer;
import io.jaegertracing.internal.samplers.ConstSampler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class InitiatorApplication {

  public static void main(String[] args) {
    SpringApplication.run(InitiatorApplication.class, args);
  }

  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
    return restTemplateBuilder.build();
  }

  @Bean
  public JaegerTracer jaegerTracer() {

    return new
        // Give a name to the configuration
        Configuration("initiator")
        // configure the sampler here.
        .withSampler(new SamplerConfiguration().withType(ConstSampler.TYPE).withParam(1))
        // Report and log spans? Well, yes please do!
        .withReporter(new ReporterConfiguration().withLogSpans(true))
        // Get the Tracer object.
        .getTracer();
  }
}
