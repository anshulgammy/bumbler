package com.utopiannerd.techcoaching.configuration;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class H2ConsoleWebServerConfiguration {

  private org.h2.tools.Server h2WebServer;
  private org.h2.tools.Server h2TcpServer;

  @EventListener(org.springframework.context.event.ContextRefreshedEvent.class)
  public void start() throws java.sql.SQLException {
    this.h2WebServer =
        org.h2.tools.Server.createWebServer("-webPort", "8082", "-tcpAllowOthers").start();
    this.h2TcpServer =
        org.h2.tools.Server.createTcpServer("-tcpPort", "9092", "-tcpAllowOthers").start();
  }

  @EventListener(org.springframework.context.event.ContextClosedEvent.class)
  public void stop() {
    this.h2TcpServer.stop();
    this.h2WebServer.stop();
  }
}
