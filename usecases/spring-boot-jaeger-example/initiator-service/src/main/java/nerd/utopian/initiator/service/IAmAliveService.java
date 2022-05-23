package nerd.utopian.initiator.service;

import static java.util.Objects.requireNonNull;

import io.jaegertracing.internal.JaegerTracer;
import java.util.Timer;
import java.util.TimerTask;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class IAmAliveService {

  private static final Logger LOGGER = LoggerFactory.getLogger(IAmAliveService.class);

  private JaegerTracer jaegerTracer;

  public IAmAliveService(JaegerTracer jaegerTracer) {
    this.jaegerTracer = requireNonNull(jaegerTracer, "jaegerTracer is required but is missing");
  }

  @PostConstruct
  public void isAliveService() {

    TimerTask timerTask = new IAmAliveTimerTask(jaegerTracer);

    // running timer task as daemon thread
    Timer isAliveTimer = new Timer(true);

    // will execute every 1 minute.
    isAliveTimer.scheduleAtFixedRate(timerTask, 0, 60 * 1000);

    LOGGER.info("isAliveService started...");
  }
}
