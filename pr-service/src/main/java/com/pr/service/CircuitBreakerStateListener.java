
package com.pr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;

@Component
public class CircuitBreakerStateListener {
	

    @Autowired
    public CircuitBreakerStateListener(
            CircuitBreakerRegistry registry) {

        registry.circuitBreaker("budgetService")
                .getEventPublisher()
                .onStateTransition(event -> {

                   System.out.println(
                            "State changed from {} to {}"+
                            event.getStateTransition()
                                    .getFromState()+
                            event.getStateTransition()
                                    .getToState()
                    );
                });
    }

}
