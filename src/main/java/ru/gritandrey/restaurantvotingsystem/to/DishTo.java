package ru.gritandrey.restaurantvotingsystem.to;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Value
@ToString(callSuper = true)
@Jacksonized
@SuperBuilder
public class DishTo extends BaseTo {

    @Schema(hidden = true)
    LocalDate date;

    @Positive
    @NotNull
    BigDecimal price;

    @NotBlank
    @Size(min = 2, max = 128)
    String name;

    @NotNull
    Integer restaurantId;
}