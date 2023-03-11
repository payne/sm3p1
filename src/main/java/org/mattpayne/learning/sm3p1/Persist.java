package org.mattpayne.learning.sm3p1;


/*
 * Copyright 2015-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.mattpayne.learning.sm3p1.order.Order;
import org.mattpayne.learning.sm3p1.order.OrderDTO;
import org.mattpayne.learning.sm3p1.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.recipes.persist.PersistStateMachineHandler;
import org.springframework.statemachine.recipes.persist.PersistStateMachineHandler.PersistStateChangeListener;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;



public class Persist {

    private final PersistStateMachineHandler handler;

    @Autowired
    private OrderService orderService;

    private final PersistStateChangeListener listener = new LocalPersistStateChangeListener();

    public Persist(PersistStateMachineHandler handler) {
        this.handler = handler;
        this.handler.addPersistStateChangeListener(listener);
    }

    public String listDbEntries() {
        List<OrderDTO> orders = orderService.findAll();
        StringBuilder buf = new StringBuilder();
        for (OrderDTO order : orders) {
            buf.append(order);
            buf.append("\n");
        }
        return buf.toString();
    }

    public void change(Long order, String event) {
        OrderDTO o = orderService.get(order);
        handler.handleEventWithStateReactively(MessageBuilder
                        .withPayload(event).setHeader("order", order).build(), o.getState())
                .subscribe();
    }

    private class LocalPersistStateChangeListener implements PersistStateChangeListener {

        @Override
        public void onPersist(State<String, String> state, Message<String> message,
                              Transition<String, String> transition, StateMachine<String, String> stateMachine) {
            if (message != null && message.getHeaders().containsKey("order")) {
                Long orderId = message.getHeaders().get("order", Long.class);
                OrderDTO order = new OrderDTO(orderId, state.getId());
                orderService.update(orderId, order);
            }
        }
    }


}