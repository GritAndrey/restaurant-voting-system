package ru.gritandrey.restaurantvotingsystem.web.vote;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.gritandrey.restaurantvotingsystem.service.VoteService;
import ru.gritandrey.restaurantvotingsystem.to.VoteTo;
import ru.gritandrey.restaurantvotingsystem.util.VoteUtil;
import ru.gritandrey.restaurantvotingsystem.web.AbstractControllerTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.gritandrey.restaurantvotingsystem.RestaurantAndDishTestData.RESTAURANT1_ID;
import static ru.gritandrey.restaurantvotingsystem.RestaurantAndDishTestData.RESTAURANT2_ID;
import static ru.gritandrey.restaurantvotingsystem.UserTestData.*;
import static ru.gritandrey.restaurantvotingsystem.VoteTestData.*;

class UserVoteControllerTest extends AbstractControllerTest {
    private final VoteService voteService;
    private static final String REST_URL = UserVoteController.REST_URL + '/';

    public UserVoteControllerTest(MockMvc mockMvc, VoteService voteService) {
        super(mockMvc);
        this.voteService = voteService;
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getAllByUserId() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_TO_MATCHER.contentJson(VoteUtil.getTos(List.of(userVote))));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + ADMIN_VOTE_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_TO_MATCHER.contentJson(VoteUtil.getTo(adminVote)));
    }

    @Test
    void getUnauth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + ADMIN_VOTE_ID))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void update() throws Exception {
        final var updated = getUpdatedVote();
        voteService.setVoteEndTime(LocalTime.MAX);
        perform(MockMvcRequestBuilders.put(REST_URL + USER_VOTE_ID)
                .param("restaurantId", String.valueOf(RESTAURANT2_ID)))
                .andExpect(status().isNoContent());
        VOTE_TO_MATCHER.assertMatch(voteService.get(USER_VOTE_ID, USER_ID), VoteUtil.getTo(updated));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateWhenVoteTimeEnded() throws Exception {
        voteService.setVoteEndTime(LocalTime.MIN);
        perform(MockMvcRequestBuilders.put(REST_URL + USER_VOTE_ID)
                .param("restaurantId", String.valueOf(RESTAURANT2_ID)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = NO_VOTE_USER_EMAIL)
    void voteForRestaurant() throws Exception {
        final var voteTo = VoteTo.builder()
                .restaurantId(RESTAURANT1_ID)
                .date(LocalDate.now())
                .time(LocalTime.now())
                .build();
        final var action = perform(MockMvcRequestBuilders.post(REST_URL)
                .param("restaurantId", String.valueOf(RESTAURANT1_ID)))
                .andExpect(status().isCreated());
        final var created = VOTE_TO_MATCHER.readFromJson(action);
        final var newId = created.getId();
        voteTo.setId(newId);
        VOTE_TO_MATCHER.assertMatch(voteService.get(newId, NO_VOTE_USER_ID), voteTo);
    }
}