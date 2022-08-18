package ru.gritandrey.restaurantvotingsystem;

import ru.gritandrey.restaurantvotingsystem.model.Vote;
import ru.gritandrey.restaurantvotingsystem.to.VoteTo;

import java.time.LocalDate;
import java.time.LocalTime;

import static ru.gritandrey.restaurantvotingsystem.RestaurantAndDishTestData.restaurant1;
import static ru.gritandrey.restaurantvotingsystem.RestaurantAndDishTestData.restaurant2;
import static ru.gritandrey.restaurantvotingsystem.UserTestData.admin;
import static ru.gritandrey.restaurantvotingsystem.UserTestData.user;

public class VoteTestData {
    public static final MatcherFactory.Matcher<VoteTo> VOTE_TO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(VoteTo.class, "time");

    public static Vote userVote = new Vote();
    public static Vote adminVote = new Vote();
    public static Vote updatedVote = new Vote();
    public static final Integer USER_VOTE_ID = 100027;
    public static final Integer ADMIN_VOTE_ID = 100026;

    static {
        prepareVotes();
    }

    private static void prepareVotes() {
        userVote.setId(USER_VOTE_ID);
        userVote.setRestaurant(restaurant1);
        userVote.setUser(user);
        userVote.setDate(LocalDate.now());
        userVote.setTime(LocalTime.of(12, 0));

        adminVote.setId(ADMIN_VOTE_ID);
        adminVote.setRestaurant(restaurant1);
        adminVote.setUser(admin);
        adminVote.setDate(LocalDate.now());
        adminVote.setTime(LocalTime.of(11, 1));

        updatedVote.setId(USER_VOTE_ID);
        updatedVote.setRestaurant(restaurant2);
        updatedVote.setUser(user);
        updatedVote.setDate(LocalDate.now());
        updatedVote.setTime(LocalTime.now());
    }

    public static Vote getUpdatedVote() {
        return updatedVote;
    }
}
