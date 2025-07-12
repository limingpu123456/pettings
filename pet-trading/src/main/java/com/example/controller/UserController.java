package com.example.controller;

import com.example.domain.ContactUser;
import com.example.domain.User;
import com.example.service.AddressService;
import com.example.service.ContactService;
import com.example.service.PetService;
import com.example.service.UserService;
import com.example.utils.createUUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
//    @Qualifier("userServiceImpl")
    private UserService userService;

    @Autowired
    private PetService petService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private ContactService contactService;

    //登録前にユーザーの存在を確認する
    @GetMapping("/checkuser")
    public Map checkuser(@RequestParam(value = "telphone",required = false)String telphone, @RequestParam(value = "idcard",required = false)String idcard){
        Map map = new HashMap();
        User user = new User();
        if (telphone != null) {
            map.put("telphone",telphone);
            user = userService.queryUser(map);
            map.remove("telphone");
            if(user != null){
                map.put("flag",0);
                map.put("msg","この電話番号はすでに登録されています");
                return map;
            }else {
                map.put("flag",1);
                map.put("msg","次へ進めます");
                return map;
            }
        }else {
            map.put("idcard",idcard);
            user = userService.queryUser(map);
            map.remove("idcard");
            if (user != null){
                map.put("flag",0);
                map.put("msg","この身分証番号はすでに登録されています");
                return map;
            }else {
                map.put("flag",1);
                map.put("msg"," 確認済みです。登録可能です");
                return map;
            }
        }
    }

    //アカウントを登録する（注册账号）
    @PostMapping("/register")
    public Map register(User user){
        Map map = new HashMap();
        String path = "defaultavatar/avatar1.jpeg";
        user.setAvatar(path);
        user.setSex("非公開");
        user.setType(1);
        userService.addUser(user);
        map.put("flag",1);
        map.put("msg","登録完了");
        return map;
    }

    //ログイン（登录）
    @GetMapping("/login")
    public Map login(String username, String password){
        //そのユーザー名が存在するかを確認する（查找该用户名(即该账户)是否存在）
        Map map = new HashMap();
        map.put("username", username);
        map.put("telphone", username);
        map.put("email", username);
        User user = userService.queryUser(map);
        map.remove("username");
        map.remove("telphone");
        map.remove("email");
        if (user == null){
            map.put("flag",0);
            map.put("msg","ユーザーが存在しません");
            return map;
        }else if (password.equals(user.getPassword())){
            map.put("flag",1);
            map.put("uid",user.getUid());
            map.put("avatar",user.getAvatar());
            map.put("type",user.getType());
            map.put("msg","ようこそ，"+user.getUsername());
            return map;
        }else {
            map.put("flag",0);
            map.put("msg"," パスワードが間違っています");
            return map;
        }
    }

    //単一ユーザーの情報を検索する
    @GetMapping("/queryuser")
    public User queryuser(Long uid){
        Map map = new HashMap();
        map.put("uid",uid);
        User user = userService.queryUser(map);
        if(user.getType() == 1){
            String idcard = user.getIdcard();
            String start = idcard.substring(0,1);
            String end = idcard.substring(idcard.length()-1);
            String center = idcard.substring(1,idcard.length()-1);
            String newidcard = start + center.replaceAll("[0-9Xx]","*") + end;
            user.setIdcard(newidcard);
        }
        return user;
    }

    //ユーザー情報を修正する
    @PostMapping("/updateuser")
    public Map updateuser(@RequestParam(name = "file",required = false) MultipartFile file, User user) throws IOException {
        Map map = new HashMap();
        User uuser = null;
        if(user.getTelphone() != null){
            map.put("telphone",user.getTelphone());
            uuser = userService.queryUser(map);
            map.remove("telphone");
            if(uuser != null && uuser.getUid() != user.getUid()){
                map.put("flag",0);
                map.put("msg","この電話番号はすでに登録されています");
                return map;
            }
        }
        if(user.getUsername() != null){
            map.put("username",user.getUsername());
            uuser = userService.queryUser(map);
            map.remove("username");
        }
        if(uuser == null || uuser.getUid() == user.getUid()){
            if(file != null) {
                if(!file.isEmpty()) {
                    //現在のプロジェクトパスを取得する（获取当前项目路径）
                    String path = System.getProperty("user.dir") + "\\src\\main\\resources\\resources\\avatar\\";
                    //ファイル名を取得し、名前の重複を防ぐ（ふせぐ）ために前に UUID を付加（ふか）します。
                    //获取文件名字,前面拼接UUID是为了防止名字重复
                    String filename = createUUID.getUUID() + file.getOriginalFilename();
                    //ファイルオブジェクトを作成し、保存先のパスを設定する
                    //创建文件对象，设置文件保存路径
                    File dest = new File(path + filename);
                    // ファイルオブジェクトを実際のファイルに変換する
                    //将文件对象转化为文件
                    file.transferTo(dest);
                    //アバターのパスを保存する
                    //存入头像地址
                    user.setAvatar("avatar/" + filename);
                    //古いユーザー情報を取得する
                    //查找旧的用户信息
                    map.put("uid",user.getUid());
                    uuser = userService.queryUser(map);
                    map.remove("uid");
                    String oldavatar = uuser.getAvatar();
                    int index = oldavatar.indexOf("defaultavatar/");
                    //以前のアバターがデフォルトかどうかを判定し、デフォルトでない場合は削除する
                    //判断旧头像是否为默认头像，不是则删除旧头像
                    if(index == -1) {
                        //以前のアバターのパスを取得する（得到旧头像的地址）
                        String oldpath = System.getProperty("user.dir") + "\\src\\main\\resources\\resources\\" + oldavatar.replace("/", "\\");
                        File olddest = new File(oldpath);
                        //以前のアバターを削除する（删除旧头像）
                        olddest.delete();
                    }
                }
            }
            userService.updateUser(user);
            map.put("avatar",user.getAvatar());
            map.put("flag",1);
            map.put("msg","修正完了");
        }else {
            map.put("flag",0);
            map.put("msg","ユーザー名はすでに存在しています");
        }
        return map;
    }

    //パスワードを変更する（修改密码）
    @PostMapping("/changepassword")
    public Map changepsaaword(User user,@RequestParam(value = "oldpassword")String oldpassword){
        Map map = new HashMap();
        map.put("uid",user.getUid());
        User user1 = userService.queryUser(map);
        map.remove("uid");
        if(user1.getPassword().equals(oldpassword)){
            userService.updateUser(user);
            map.put("flag",1);
            map.put("msg","変更に成功しました");
            return map;
        }else {
            map.put("flag",0);
            map.put("msg","パスワードが間違っています");
            return map;
        }
    }

    //ユーザーを削除する
    @GetMapping("/deleteUser")
    public String deleteUser(Long uid){
        Map map = new HashMap();
        map.put("uid",uid);
        userService.deleteUser(map);
        petService.deletePet(map);
        addressService.deleteAddress(map);
        map.remove("uid");
        map.put("fromuid",uid);
        contactService.deleteContact(map);
        return "ユーザーは削除済みです";
    }

    //すべてのユーザーを検索する（查找所有用户）
    @GetMapping("/queryalluser")
    public List<User> querAllUser(@RequestParam(name = "page",required = false) Integer page,@RequestParam(name = "count",required = false) Integer count){
        Map map = new HashMap();
        if (page != null && count != null) {
            map.put("min",(page-1)*count);
            map.put("max",count);
        }
        List<User> user = userService.queryAllUser(map);
        return user;
    }

    @GetMapping("/countuser")
    public int countuser() {
        Map map = new HashMap();
        return userService.countuser(map);
    }

    @GetMapping("/querycontactuser")
    public List<ContactUser> querycontactuser(Long uid){
        return userService.querycontactuser(uid);
    }

    @GetMapping("/queryuserby")
    public Map queryuserby(User user){
        Map map = new HashMap();
        if(user.getUid() != null){
            map.put("uid",user.getUid());
        }
        if(user.getUsername() != null && user.getUsername() != ""){
            map.put("username",user.getUsername());
        }
        if(user.getRealname() != null && user.getRealname() != ""){
            map.put("realname",user.getRealname());
        }
        if(user.getIdcard() != null && user.getIdcard() != ""){
            map.put("idcard",user.getIdcard());
        }
        if(user.getTelphone() != null && user.getTelphone() != ""){
            map.put("telphone",user.getTelphone());
        }
        Map map1 = new HashMap();
        map1.put("total",userService.countuser(map));
        map1.put("user",userService.queryuserby(map));
        return map1;
    }
}
