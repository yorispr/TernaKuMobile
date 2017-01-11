package com.fintech.ternaku.TernakPerah.Main.NavBar.CalendarToDoList;

import com.p_v.flexiblecalendar.entity.Event;

/**
 * Created by Pandhu on 10/24/16.
 */

public class CustomEvent implements Event {

    private int color;

    public CustomEvent(int color){
        this.color = color;
    }

    @Override
    public int getColor() {
        return color;
    }
}