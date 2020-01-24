package com.team2502.timetracker.slack;

import com.team2502.timetracker.internal.JsonData;
import org.riversun.slacklet.Slacklet;
import org.riversun.slacklet.SlackletRequest;
import org.riversun.slacklet.SlackletResponse;
import org.riversun.slacklet.SlackletService;

import java.io.IOException;

@SuppressWarnings("All")
public class SlackApp {

    private static final String TOKEN = "xoxb-128117363280-908670379523-a5cmaOGBp2L38gg3Zacm5zLW";

    private final SlackletService slack;
    private final JsonData timeTracker;

    public SlackApp(String token, JsonData timeTracker) {
        this.timeTracker = timeTracker;
        this.slack = new SlackletService(token);
        this.slack.addSlacklet(new Slacklet() {
            @Override
            public void onMessagePosted(SlackletRequest req, SlackletResponse resp) {
                if(req.getSender().isBot())
                    return;

                resp.reply("Hello World!");
            }
        });
    }

    public void start() throws IOException {
        this.slack.start();
    }

    public void stop() throws IOException {
        slack.stop();
    }

    public static void main(String[] args) throws IOException{
        SlackApp app = new SlackApp(TOKEN, new JsonData("data.json"));
        app.start();
    }
}