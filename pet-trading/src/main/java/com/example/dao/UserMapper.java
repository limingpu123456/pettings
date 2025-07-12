package com.example.dao;

import com.example.domain.ContactUser;
import com.example.domain.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Mapper
public interface UserMapper {

    //ユーザーを追加する（增加用户）
    int addUser(User user);

    //単一ユーザーを検索する（查找单个用户）
    User queryUser (Map map);

    //ユーザー情報を更新する（修改用户信息）
    int updateUser(User user);

    //ユーザーを削除する（删除用户）
    int deleteUser(Map map);

    //すべての一般ユーザーを検索する（查找所有普通用户）
    List<User> queryAllUser(Map map);

    //ユーザー数をカウントする
    int countuser(Map map);

    //ユーザーの最近の連絡先を取得する（查找用户最近联系人）
    List<ContactUser> querycontactuser(Long uid);

    //条件に基づいてユーザーを検索する（更加条件查询用户）
    List<User> queryuserby(Map map);
}
