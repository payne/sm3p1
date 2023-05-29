package org.mattpayne.learning.sm3p1;

import org.mattpayne.learning.sm3p1.order.OrderDTO;
import org.mattpayne.learning.sm3p1.order.OrderService;
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

    @Autowired
    private OrderService orderService;


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

    public static void main(final String[] args) {
        SpringApplication.run(Sm3p1Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setState("PROCESS");
        Long id = orderService.create(orderDTO);
        orderDTO = orderService.get(id);
        persist.change(id, "PROCESS");
        String msg = persist.listDbEntries();
        System.out.println("Msg from persist.listDbEntries(): " + msg);
        State<String, String> currentState = stateMachine.getState();
        System.out.println("Current state: " + currentState.getId());

        persist.change(id, "SEND");
        System.out.println(persist.listDbEntries());
                currentState = stateMachine.getState();
        System.out.println("Current state: " + currentState.getId());
    }
    

}