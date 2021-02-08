# blog
###Dev branch

<<<<<<< HEAD
### Post view
      CREATE VIEW post_for_request
       AS
       SELECT post.id,
=======
CREATE TABLE users
(
    id           int          NOT NULL AUTO_INCREMENT,
    is_moderator TINYINT      NOT NULL,
    reg_time     datetime     not null,
    name         VARCHAR(255) NOT NULL,
    email        VARCHAR(255) NOT NULL,
    password     VARCHAR(255) NOT NULL,
    code         VARCHAR(255),
    photo        TEXT,
    PRIMARY KEY (id)
);

CREATE TABLE posts
(
    id                int                                  NOT NULL AUTO_INCREMENT,
    is_active         TINYINT                              NOT NULL,
    moderation_status ENUM ('NEW', 'ACCEPTED', 'DECLINED') NOT NULL,
    moderator_id      int                                  NOT NULL,
    user_id           int                                  NOT NULL,
    time              datetime,
    title             VARCHAR(255)                         NOT NULL,
    text              text,
    view_count        int                                  NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (moderator_id) REFERENCES users (id)
);

CREATE TABLE posts_votes
(
    id      int      NOT NULL AUTO_INCREMENT,
    user_id int      NOT NULL,
    post_id int      NOT NULL,
    time    DATETIME NOT NULL,
    value   TINYINT  NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (post_id) REFERENCES posts (id)
);

CREATE TABLE tags
(
    id   int          NOT NULL,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE tag2post
(
    id      int NOT NULL AUTO_INCREMENT,
    post_id int NOT NULL,
    tag_id  int NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (post_id) REFERENCES posts (id),
    FOREIGN KEY (tag_id) REFERENCES tags (id)
);

CREATE TABLE post_comments
(
    id        int      NOT NULL AUTO_INCREMENT,
    parent_id int,
    post_id   int      NOT NULL,
    user_id   int      NOT NULL,
    time      DATETIME NOT NULL,
    text      TEXT     NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (parent_id) REFERENCES post_comments (id),
    FOREIGN KEY (post_id) REFERENCES posts (id),
    FOREIGN KEY (user_id) REFERENCES users (id)

);

CREATE TABLE captcha_codes
(
    id          int      NOT NULL AUTO_INCREMENT,
    time        DATETIME NOT NULL,
    code        TINYTEXT NOT NULL,
    secret_code TINYTEXT NOT NULL,
    PRIMARY KEY (id)
);
CREATE TABLE global_settings
(
    id    int          NOT NULL AUTO_INCREMENT,
    code  VARCHAR(255) NOT NULL,
    name  VARCHAR(255) NOT NULL,
    value VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);


DROP TABLE captcha_codes;
DROP TABLE global_settings;
DROP TABLE post_comments;
DROP TABLE posts;
DROP TABLE posts_votes;
DROP TABLE tag2post;
DROP TABLE tags;
DROP TABLE users;

CREATE VIEW post_for_request
AS
SELECT post.id,
>>>>>>> origin/dev
       UNIX_TIMESTAMP(post.time)                                                 as timestamp,
       user.id                                                                   as user_id,
       user.name,
       post.title,
       post.text                                                                 as announce,
       (SELECT COUNT(*) FROM post_comments WHERE post_id = post.id)              as comment_count,
       (SELECT COUNT(*) FROM posts_votes WHERE post_id = post.id AND value = 1)  as like_count,
       (SELECT COUNT(*) FROM posts_votes WHERE post_id = post.id AND value = -1) as dislike_Count,
       post.view_count
<<<<<<< HEAD
    FROM posts post
         INNER JOIN users user on post.user_id = user.id; 
=======
FROM posts post
         INNER JOIN users user on post.user_id = user.id;

DROP VIEW post_for_request;
>>>>>>> origin/dev
