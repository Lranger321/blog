package main.dto.response;

import lombok.Data;

@Data
public class StatisticsResponse {

    private Long postsCount;

    private Long likesCount;

    private Long dislikesCount;

    private Long viewsCount;

    private Long firstPublication;

}
