package nerd.utopian.initiator.service;

import io.opentracing.Span;
import io.opentracing.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimerTask;

public class IAmAliveTimerTask extends TimerTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(IAmAliveTimerTask.class);

    private Tracer tracer;

    public IAmAliveTimerTask(Tracer tracer) {
        this.tracer = tracer;
    }

    @Override
    public void run() {

        // creating a new span.
        Span span = tracer.buildSpan("iam-alive").start();

        // marking the span as child of...
        Span activeSpan = tracer.buildSpan("iam-alive-call").asChildOf(span).start();

        LOGGER.info("initiator-service is alive");

        activeSpan.finish();
    }
}
