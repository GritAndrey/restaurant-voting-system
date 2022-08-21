package ru.gritandrey.restaurantvotingsystem.web.controller.restaurant;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.gritandrey.restaurantvotingsystem.service.RestaurantService;
import ru.gritandrey.restaurantvotingsystem.to.RestaurantTo;
import ru.gritandrey.restaurantvotingsystem.to.RestaurantWithMenuTo;
import ru.gritandrey.restaurantvotingsystem.util.mapper.RestaurantMapper;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = UserRestaurantRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@RequiredArgsConstructor
@Tags({@Tag(name = "User restaurant controller", description = "View available restaurants. With or without menu.")})
public class UserRestaurantRestController {

    public static final String REST_URL = "/api/rest/restaurants";
    public static final int DEFAULT_PAGE = 0;
    public static final int DEFAULT_ITEMS_PER_PAGE = 4;
    private final RestaurantService restaurantService;

    @GetMapping("/{id}")
    public RestaurantTo get(@PathVariable int id) {
        final var restaurant = restaurantService.get(id);
        log.info("Get Restaurant with id {} without menu: {}", restaurant.getId(), restaurant);
        return restaurant;
    }

    @GetMapping
    public Page<RestaurantTo> getAll(@RequestParam(required = false) Integer page,
                                     @RequestParam(required = false) Integer itemsPerPage) {
        page = Optional.ofNullable(page).orElse(DEFAULT_PAGE);
        itemsPerPage = Optional.ofNullable(itemsPerPage).orElse(DEFAULT_ITEMS_PER_PAGE);
        final var restaurants = restaurantService.getAll(page, itemsPerPage);
        log.info("GetAll Restaurants without menu: {}", restaurants);
        return restaurants.map(RestaurantMapper::getTo);
    }

    @GetMapping("/{id}/menu")
    public RestaurantWithMenuTo getWithMenu(@PathVariable int id) {
        final RestaurantWithMenuTo restaurant = restaurantService.getWithMenu(id);
        log.info("Get Restaurant with id {} with menu on today: {}", restaurant.getId(), restaurant);
        return restaurant;
    }

    @GetMapping("/menu")
    public List<RestaurantWithMenuTo> getAllWithMenu() {
        final List<RestaurantWithMenuTo> restaurants = restaurantService.getAllWithMenu();
        log.info("GetAll Restaurants with menu on today: {}", restaurants);
        return restaurants;
    }
}
