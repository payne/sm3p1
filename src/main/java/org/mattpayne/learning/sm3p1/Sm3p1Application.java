package org.mattpayne.learning.sm3p1;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.recipes.persist.PersistStateMachineHandler;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.statemachine.state.State;


@SpringBootApplication
public class Sm3p1Application implements CommandLineRunner {


    @Autowired
    private StateMachine<String, String> stateMachine;

    @Autowired
    private Persist persist;

    /*
    //tag::snippetA[]
    @Configuration
    @EnableStateMachine
    static class StateMachineConfig
            extends StateMachineConfigurerAdapter<String, String> {

        @Override
        public void configure(StateMachineStateConfigurer<String, String> states)
                throws Exception {
            states
                    .withStates()
                    .initial("PLACED")
                    .state("PROCESSING")
                    .state("SENT")
                    .state("DELIVERED");
        }

        @Override
        public void configure(StateMachineTransitionConfigurer<String, String> transitions)
                throws Exception {
            transitions
                    .withExternal()
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
        }

    }
//end::snippetA[]
     */

    //tag::snippetB[]
    @Configuration
    static class PersistHandlerConfig {

        @Autowired
        private StateMachine<String, String> stateMachine;

        @Bean
        public Persist persist() {
            return new Persist(persistStateMachineHandler());
        }

        @Bean
        public PersistStateMachineHandler persistStateMachineHandler() {
            return new PersistStateMachineHandler(stateMachine);
        }

    }
//end::snippetB[]

    //tag::snippetC[]
//end::snippetC[]


//     public static void main(String[] args) throws Exception {
//         Bootstrap.main(args);
//    }

    public static void main(final String[] args) {
        SpringApplication.run(Sm3p1Application.class, args);
    }
    @Override
    public void run(String... args) throws Exception {

        // persist.listDbEntries();


    }
   public void runOLD(String... args) throws Exception {
        stateMachine.start();
        stateMachine.sendEvent("PROCESS");
        stateMachine.sendEvent("SEND");
        stateMachine.sendEvent("DELIVER");

        State<String, String> currentState = stateMachine.getState();
        System.out.println("Current state: " + currentState.getId());

        stateMachine.stop();

    }

}