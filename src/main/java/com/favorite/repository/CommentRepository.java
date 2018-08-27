package com.favorite.repository;

import com.favorite.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by cdc on 2018/8/16.
 */
public interface CommentRepository extends JpaRepository<Comment,Long> {

    public String findReplyUserSql="select u.id as userId, u.userName as userName,u.profilePicture as profilePicture,c.content as content," +
            " c.createTime as createTime,c.replyUserId as replyUserId "
            + "from Comment c,User u WHERE c.userId=u.id";

    Long countByCollectId(Long collectId);
}
