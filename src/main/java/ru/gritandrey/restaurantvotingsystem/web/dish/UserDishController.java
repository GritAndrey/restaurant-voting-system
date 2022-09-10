package ru.gritandrey.restaurantvotingsystem.web.dish;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import ru.gritandrey.restaurantvotingsystem.service.DishService;
import ru.gritandrey.restaurantvotingsystem.to.DishFilter;
import ru.gritandrey.restaurantvotingsystem.to.MenuTo;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = UserDishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
@Tags({@Tag(name = "User Dish controller", description = "Get dishes by restaurantId and(or) date")})
public class UserDishController {
    public static final String REST_URL = "/api/restaurants/{restaurantId}/dishes";
    private final DishService dishService;

    @GetMapping
    @Operation(summary = "GetAll dishes by Restaurant Id and period. If all params are null, return all menu for today")
    public List<MenuTo> getAllBy(@PathVariable Integer restaurantId,
                                 @RequestParam @Nullable
                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                 @RequestParam @Nullable
                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        if (startDate == null && endDate == null) {
            startDate = LocalDate.now();
        }
        log.info("GetAll dishes by Restaurant Id: {} and startDate: {} , endDate: {}", restaurantId, startDate, endDate);
        return dishService.getByFilter(new DishFilter(restaurantId, startDate, endDate));
    }
}
