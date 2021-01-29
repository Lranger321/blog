# blog
###Dev branch

### Post view
      CREATE VIEW post_for_request
       AS
       SELECT post.id,
       UNIX_TIMESTAMP(post.time)                                                 as timestamp,
       user.id                                                                   as user_id,
       user.name,
       post.title,
       post.text                                                                 as announce,
       (SELECT COUNT(*) FROM post_comments WHERE post_id = post.id)              as comment_count,
       (SELECT COUNT(*) FROM posts_votes WHERE post_id = post.id AND value = 1)  as like_count,
       (SELECT COUNT(*) FROM posts_votes WHERE post_id = post.id AND value = -1) as dislike_Count,
       post.view_count
    FROM posts post
         INNER JOIN users user on post.user_id = user.id; 
