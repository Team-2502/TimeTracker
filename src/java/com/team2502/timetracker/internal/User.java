package com.team2502.timetracker.internal;


public class User {

    private final String name;
    private final long totalTime;

    public User(String name, long totalTime) {
        this.name = name;
        this.totalTime = totalTime;
    }

    public String getName()
    {
        return name;
    }

    public long getTotalTime()
    {
        return totalTime;
    }
}