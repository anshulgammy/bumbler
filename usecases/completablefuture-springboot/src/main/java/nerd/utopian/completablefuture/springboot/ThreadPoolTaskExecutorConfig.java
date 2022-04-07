package nerd.utopian.completablefuture.springboot;

import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class ThreadPoolTaskExecutorConfig {

  @Bean("customThreadPoolTaskExecutor")
  public Executor getThreadPoolTaskExecutor() {
    ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
    threadPoolTaskExecutor.setCorePoolSize(2);
    threadPoolTaskExecutor.setMaxPoolSize(2);
    threadPoolTaskExecutor.setQueueCapacity(100);
    threadPoolTaskExecutor.setThreadNamePrefix("utopian-thread-");
    threadPoolTaskExecutor.initialize();
    return threadPoolTaskExecutor;
  }
}
