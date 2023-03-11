package org.mattpayne.learning.sm3p1;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import java.util.Arrays;
import java.util.HashSet;

@Configuration
@EnableStateMachine(name = "simpleStateMachine")
public class SimpleStateMachineConfiguration {


    @Bean
    public StateMachine<String, String> stateMachine(StateMachineListener<String, String> listener) throws Exception {
        StateMachineBuilder.Builder<String, String> builder = StateMachineBuilder.builder();
        builder.configureStates().withStates()
                .initial("PLACED")
                .states(new HashSet<>(Arrays.asList("PROCESSING", "SENT", "DELIVERED")));

        builder.configureTransitions().withExternal()
                .source("PLACED").target("PROCESSING")
                .event("PROCESS")
                .and()
                .withExternal()
                .source("PROCESSING").target("SENT")
                .event("SEND")
                .and()
                .withExternal()
                .source("SENT").target("DELIVERED")
                .event("DELIVER");


        StateMachine<String, String> stateMachine = builder.build();
        stateMachine.addStateListener(listener);
        return stateMachine;
    }

    @Bean
    public StateMachineListener<String, String> listener() {
        return new StateMachineListenerAdapter<String, String>() {
            @Override
            public void stateChanged(State<String, String> from, State<String, String> to) {
                System.out.println("State change to " + to);
            }
        };
    }

}