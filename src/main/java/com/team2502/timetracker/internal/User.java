package com.team2502.timetracker.internal;

@SuppressWarnings("All")
public class User {

    public final String name;
    public final long totalTime;

    public User(String name, long totalTime) {
        this.name = name;
        this.totalTime = totalTime;
    }
}