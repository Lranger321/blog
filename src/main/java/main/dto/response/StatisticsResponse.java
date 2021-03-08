package main.dto.response;

import lombok.Data;

@Data
public class StatisticsResponse {

    private long postCount;

    private long likesCount;

    private long dislikeCount;

    private long viewCount;

    private long firstPublication;

}
