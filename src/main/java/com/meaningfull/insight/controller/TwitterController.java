//package com.example.demo.controller;
//
//import com.nerafox.tickeer.services.TwitterService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
///**
// * Created by Shahaf Pariente on 7/21/2020
// */
//@RestController
//@RequestMapping({"/tweets"})
//public class TwitterController {
//
//    private final Logger logger = LoggerFactory.getLogger(this.getClass());
//    final TwitterService twitterService;
//
//    public TwitterController(TwitterService twitterService) {
//        this.twitterService = twitterService;
//    }
//
//    @GetMapping({"/getTweet","/getTweet/"})
//    public List<Object> getTweetsForUser(@RequestParam String user) {
//        logger.info("Get Tweets for: " + user);
//        return twitterService.getTweetsForUser(user);
//    }
//
//    @GetMapping({"/getTweetByHash","/getTweetByHash/"})
//    public List<Object> getTweetsByHashtag(@RequestParam String hashtag) {
//        logger.info("Get Tweets for: " + hashtag);
//        return twitterService.getTweetsByHashtag(hashtag);
//    }
//}
