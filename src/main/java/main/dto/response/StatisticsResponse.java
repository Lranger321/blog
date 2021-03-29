package main.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsResponse {

    private Long postsCount;

    private Long likesCount;

    private Long dislikesCount;

    private Long viewsCount;

    private Long firstPublication;

}
