package com.theta.android.sudokuapp;

import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.PositionAssertions.isCompletelyLeftOf;
import static androidx.test.espresso.assertion.PositionAssertions.isCompletelyRightOf;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Test;
import org.junit.Rule;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest

public class EspressoTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
    new ActivityScenarioRule<MainActivity>(MainActivity.class);

    @Test
    //check if statistic button is left of settings button
    public void statisticsDisplayedLeftSettings () {
        //check button displayed
        onView(withId(R.id.statistics)).check(matches(isDisplayed()));
        //is to the left?
        onView(withId(R.id.statistics)).check(isCompletelyLeftOf(withId(R.id.settings)));
    }
    @Test
    //check if statistic button is left of settings button
    public void statisticsDisplayedRightSettings () {
        //check button displayed
        onView(withId(R.id.help)).check(matches(isDisplayed()));
        //is to the right?
        onView(withId(R.id.help)).check(isCompletelyRightOf(withId(R.id.settings)));
        //is to the left of? (creates an error)
        //onView(withId(R.id.help)).check(isCompletelyLeftOf(withId(R.id.settings)));
    }

    @Test
    //test to click all the buttons in MainActivity
    public void ClickButtons() {
        onView(withId(R.id.statistics)).perform(click());
        Espresso.pressBack();
        onView(withId(R.id.settings)).perform(click());
        Espresso.pressBack();
        onView(withId(R.id.help)).perform(click());
        Espresso.pressBack();
    }

    @Test
    //puts the number 9 in the top left corner
    public void NineInTop() {
        onView(withId(R.id.statistics)).perform(click());

    }
}
