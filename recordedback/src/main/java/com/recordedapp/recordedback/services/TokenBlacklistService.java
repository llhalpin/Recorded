package com.recordedapp.recordedback.services;

import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.*;

//llh 06-04-2025 In-memory storage for blacklisted tokens for testing but consider database or Redis if going to production
@Service
public class TokenBlacklistService {

    private final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    //constructor - it clean up expired tokens every hour
    public TokenBlacklistService() {
        scheduler.scheduleAtFixedRate(this::cleanupExpiredTokens, 1, 1, TimeUnit.HOURS);
    }

    //add expired token to list of expired tokens
    public void blacklistToken(String token){
        blacklistedTokens.add(token);
        System.out.print("Token blacklisted: "+ token.substring(0,10) + "...");
    }

    //function return boolean of whether token is in the list of expired tokens
    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }

    private void cleanupExpiredTokens(){
        System.out.println("Cleaning up expired blacklisted tokens...");
        //llh!!! need to implement function still
    }



}
