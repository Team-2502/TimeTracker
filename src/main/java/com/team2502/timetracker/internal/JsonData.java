package com.team2502.timetracker.internal;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;

/**
 * Must start with this in json file:
 * {"users":{}}
 *
 * Json data format:
 * {"users": {
 *     "Biil N" : {
 *         "time_data": [
 *             {
 *                 "start": "2020-01-24T16:07:20.559",
 *                 "end": "2020-01-24T16:08:05.936"
 *             }
 *         ],
 *         "logged_in": true,
 *         "total_time": 87342
 *     }
 * }}
 */

@SuppressWarnings("All")
public class JsonData {

    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private final String filePath;
    private final JSONObject data;
    private final JSONObject users;

    public JsonData(String filePath) throws IOException {
        this.filePath = filePath;
        this.data = JsonStorage.readJsonFromFile(filePath);
        this.users = this.data.getJSONObject("users");
    }

    public boolean userExists(String name) {
        return users.has(name);
    }

    public void createUser(String name) {
        users.put(name, new JSONObject()
                .put("logged_in", false)
                .put("total_time", 0)
                .put("time_data", new JSONArray()));
    }

    public void toggleUserLogin(String name) {
        if(!users.has(name))
            throw new IllegalArgumentException("User does not exist");

        boolean loggedIn = userIsLoggedIn(name);
        JSONObject user = users.getJSONObject(name);
        JSONArray timeData = user.getJSONArray("time_data");

        if(loggedIn) {
            JSONObject timeDataObj = timeData.getJSONObject(timeData.length()-1);
            LocalDateTime start = LocalDateTime.parse(timeDataObj.getString("start"));
            LocalDateTime end = LocalDateTime.now();

            timeDataObj.put("end", end.format(TIME_FORMAT));
            users.getJSONObject(name).put("total_time", getUserTotalTime(name)+ChronoUnit.MINUTES.between(start, end));
        }else timeData.put(new JSONObject().put("start", LocalDateTime.now().format(TIME_FORMAT)));

        users.getJSONObject(name).put("logged_in", !loggedIn);
    }

    public void addTimeData(String name, LocalDateTime start, LocalDateTime end) {
        if(!users.has(name))
            throw new IllegalArgumentException("User does not exist");

        JSONObject timeData = new JSONObject();
        timeData.put("start", start.format(TIME_FORMAT));
        timeData.put("end", end.format(TIME_FORMAT));
        users.getJSONObject(name).getJSONArray("time_data").put(timeData);
    }

    public void recalculateTotalTimes() {
        Iterator<String> keys = users.keys();
        while(keys.hasNext()) {
            JSONObject user = users.getJSONObject(keys.next());
            JSONArray timeDataArray = user.getJSONArray("time_data");

            long totalTime = 0;
            for(int i = 0; i < timeDataArray.length(); i++) {
                JSONObject timeData = timeDataArray.getJSONObject(i);
                if(!timeData.has("end"))
                    continue;

                totalTime += ChronoUnit.MINUTES.between(
                        LocalDateTime.parse(timeData.getString("start")),
                        LocalDateTime.parse(timeData.getString("end")));
            }

            user.put("total_time", totalTime);
        }
    }

    public boolean userIsLoggedIn(String name) {
        if(!users.has(name))
            throw new IllegalArgumentException("User does not exist");

        return users.getJSONObject(name).getBoolean("logged_in");
    }

    public long getUserTotalTime(String name) {
        if(!users.has(name))
            throw new IllegalArgumentException("User does not exist");

        return users.getJSONObject(name).getLong("total_time");
    }

    public String[] getUsers() {
        String[] names = new String[users.keySet().size()];
        Iterator<String> keys = users.keys();
        for(int i = 0; keys.hasNext(); i++)
            names[i] = keys.next();

        return names;
    }

    public void store() throws IOException {
        JsonStorage.writeJsonToFile(data, filePath);
    }
}