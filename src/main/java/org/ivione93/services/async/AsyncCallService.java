package org.ivione93.services.async;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.context.ManagedExecutor;

import java.util.concurrent.CompletionException;

@ApplicationScoped
public abstract class AsyncCallService {

  @ConfigProperty(name = "api.timeout.milliseconds")
  int timeoutMilliseconds;

  @Inject
  ManagedExecutor managedExecutor;

  CompletionException toCompletionException(final Throwable ex) {
    return ex instanceof CompletionException
            ? (CompletionException) ex
            : new CompletionException(ex);
  }

}
