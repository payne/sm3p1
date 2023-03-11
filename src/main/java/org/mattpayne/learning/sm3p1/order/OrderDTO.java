package org.mattpayne.learning.sm3p1.order;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String state;

}
