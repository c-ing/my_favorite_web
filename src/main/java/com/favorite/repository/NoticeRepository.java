package com.favorite.repository;

import com.favorite.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by cdc on 2018/8/16.
 */
public interface NoticeRepository extends JpaRepository<Notice,Long> {

    public String baseSql="select c.id as id,c.title as title, c.type as type,c.url as url," +
            "c.favoritesId as favoritesId,c.remark as favoriteName,c.logoUrl as logoUrl,c.userId as userId, "
            + "c.remark as remark,c.description as description,c.lastModifyTime as lastModifyTime, "
            + "u.userName as userName,u.profilePicture as profilePicture,n.operId as operId "
            + "from Notice n,Collect c,User u WHERE n.collectId=c.id and c.userId=u.id";

    Long countByUserIdAndTypeAndReaded(Long userId,String type,String readed);

    @Query("select count(1) from Notice n,Praise p where n.operId=p.id and type='praise' and n.userId=?1 and n.readed=?2")
    Long countPraiseByUserIdAndReaded(Long userId,String readed);

}
