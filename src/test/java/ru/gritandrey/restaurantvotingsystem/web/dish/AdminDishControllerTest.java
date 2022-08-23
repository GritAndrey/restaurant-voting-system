package ru.gritandrey.restaurantvotingsystem.web.dish;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.gritandrey.restaurantvotingsystem.exception.IllegalRequestDataException;
import ru.gritandrey.restaurantvotingsystem.model.Dish;
import ru.gritandrey.restaurantvotingsystem.service.DishService;
import ru.gritandrey.restaurantvotingsystem.to.DishTo;
import ru.gritandrey.restaurantvotingsystem.util.DishUtil;
import ru.gritandrey.restaurantvotingsystem.util.JsonUtil;
import ru.gritandrey.restaurantvotingsystem.web.AbstractControllerTest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.gritandrey.restaurantvotingsystem.RestaurantAndDishTestData.*;
import static ru.gritandrey.restaurantvotingsystem.UserTestData.ADMIN_MAIL;
import static ru.gritandrey.restaurantvotingsystem.UserTestData.USER_MAIL;

class AdminDishControllerTest extends AbstractControllerTest {
    private static final String REST_URL = AdminDishController.REST_URL + '/';
    private final DishService dishService;

    public AdminDishControllerTest(MockMvc mockMvc, DishService dishService) {
        super(mockMvc);
        this.dishService = dishService;
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + DISH1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_TO_MATCHER.contentJson(dish1To));
    }

    @Test
    void getUnauth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + NOT_FOUND))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createWithLocation() throws Exception {
        final var newDish = getNewDishWithExistingNameAndRestaurant();
        final var newDishTo = getNewDishTo();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newDishTo)))
                .andExpect(status().isCreated());
        DishTo created = DISH_TO_MATCHER.readFromJson(action);

        int newId = created.id();
        newDish.setId(newId);
        DISH_MATCHER.assertMatch(dishService.get(newId), newDish);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + DISH1_ID))
                .andExpect(status().isNoContent());
        assertThrows(IllegalRequestDataException.class, () -> dishService.get(DISH1_ID));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + NOT_FOUND))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void update() throws Exception {
        Dish updated = getUpdatedDish();
        perform(MockMvcRequestBuilders.put(REST_URL + DISH1_ID).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(DishUtil.getTo(updated))))
                .andDo(print())
                .andExpect(status().isNoContent());
        DISH_MATCHER.assertMatch(dishService.get(DISH1_ID), updated);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateNotFound() throws Exception {
        Dish updated = getUpdatedDish();
        updated.setId(NOT_FOUND);
        perform(MockMvcRequestBuilders.put(REST_URL + NOT_FOUND).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(DishUtil.getTo(updated))))
                .andExpect(status().isUnprocessableEntity());
    }
}