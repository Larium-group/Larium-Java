package com.meaningfull.insight.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Shahaf Pariente on 7/21/2020
 */
@Component
public class TwitterApi {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    Twitter twitter;

    TwitterApi() {
        this.twitter = createTwitterInstance();
    }

    private Twitter createTwitterInstance() {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setDebugEnabled(true).setOAuthConsumerKey("p8lA0TpForYSBnbLCTNLpFMn3")
                .setOAuthConsumerSecret("weBxKVnLcTdanGxrzDQ2kF6WTkaIIHL43OwsaNQfmMkOgpEbE9")
                .setOAuthAccessToken("1208323412042698753-LGQw8VUOK1Jm3dORVW8HCugSVdgYeb")
                .setOAuthAccessTokenSecret("ce6QXKBRxjqgLmxhRgVvzbgS79mdvuIdgoylQBj8rTyVq");

        return new TwitterFactory(configurationBuilder.build()).getInstance();
    }

    public List<Object> getUserTweets(String user) {
        try {
            return Collections.singletonList(this.twitter.getUserTimeline(user));
        } catch (TwitterException e) {
            logger.error("Could not get the user timeline...");
            return null;
        }
    }

    public List<Object> getTweetByHashtag(String hashtag) {
        try {
            Query query = new Query(hashtag);
            query.count(10);
            QueryResult queryResult = twitter.search(query);
            return new LinkedList<>(Collections.singleton(queryResult));
        } catch (TwitterException e) {
            logger.error("Could not find tweets with this hashtag!");
            return null;
        }
    }
}
