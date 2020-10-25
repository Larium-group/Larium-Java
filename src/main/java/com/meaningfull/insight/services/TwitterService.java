//package com.example.demo.services;
//
//import com.nerafox.tickeer.api.TwitterApi;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
///**
// * Created by Shahaf Pariente on 7/21/2020
// */
//@Service
//public class TwitterService {
//
//    private final Logger logger = LoggerFactory.getLogger(this.getClass());
//    @Autowired
//    TwitterApi twitterApi;
//
//    public List<Object> getTweetsForUser(String user) {
//        return twitterApi.getUserTweets(user);
//    }
//
//    public List<Object> getTweetsByHashtag(String hashtag) {
//        return twitterApi.getTweetByHashtag(hashtag);
//    }
//
//
//}
