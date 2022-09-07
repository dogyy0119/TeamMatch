package com.gs.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class PUBGConfig {

    @Value("${pubg.seasons}")
    private String seasons;

    @Value("${pubg.token-pull}")
    private String token_pull;

    @Value("${pubg.token}")
    private String token;

    @Value("${pubg.url.matches}")
    private String url_matches;

    @Value("${pubg.url.season}")
    private String url_season;

    @Value("${pubg.url.games}")
    private String url_games;

    @Value("${pubg.game-type}")
    private String game_type;

    @Value("${pubg.head-token}")
    private String head_token;

    @Value("${pubg.head-formate}")
    private String head_formate;


}

