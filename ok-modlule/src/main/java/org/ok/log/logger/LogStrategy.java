package org.ok.log.logger;

public interface LogStrategy {

  void log(int priority, String tag, String message);
}
