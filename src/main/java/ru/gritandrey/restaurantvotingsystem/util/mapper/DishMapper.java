package ru.gritandrey.restaurantvotingsystem.util.mapper;

import lombok.experimental.UtilityClass;
import ru.gritandrey.restaurantvotingsystem.model.Dish;
import ru.gritandrey.restaurantvotingsystem.model.Restaurant;
import ru.gritandrey.restaurantvotingsystem.to.DishTo;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

@UtilityClass
public class DishMapper {

    public static DishTo getTo(Dish dish) {
        return DishTo.builder()
                .restaurantId(dish.getRestaurant() == null ? null : dish.getRestaurant().getId())
                .name(dish.getName())
                .price(dish.getPrice())
                .id(dish.getId())
                .build();
    }

    public static List<DishTo> getTos(Collection<Dish> dishes) {
        return dishes.stream().map(DishMapper::getTo).collect(toList());
    }

    public static Dish getDish(DishTo dishTo) {
        return new Dish(dishTo.getId(), dishTo.getPrice(), dishTo.getName(), new Restaurant(), LocalDate.now());
    }

    public static List<Dish> getDishes(Collection<DishTo> dishesTo) {
        return dishesTo.stream().map(DishMapper::getDish).collect(toList());
    }
}
