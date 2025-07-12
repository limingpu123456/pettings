package com.example.controller;

import com.example.domain.Pet;
import com.example.domain.Petorder;
import com.example.domain.Photo;
import com.example.service.PetService;
import com.example.service.PetorderService;
import com.example.service.PhotoService;
import com.example.utils.createUUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.StandardSocketOptions;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pet")
public class PetController {

    @Autowired
    private PetService petService;

    @Autowired
    private PhotoService photoService;

    @Autowired
    private PetorderService petorderService;

    //ペットの販売または購入リクエストを投稿する（发布宠物出售或购买请求）
    @PostMapping("/addPet")
    public String addPet(@RequestParam(name = "files",required = false) MultipartFile[] files,Pet pet) throws IOException {
        Date date = new Date();
        pet.setDate(date);
        petService.addPet(pet);
        Photo photo = new Photo();
        photo.setPid(pet.getPid());
        if (files != null){
            if (files.length > 0){
                for (int i=0;i< files.length;i++){
                    // 現在のプロジェクトパスを取得する（获取当前项目路径）
                    String path = System.getProperty("user.dir") + "\\src\\main\\resources\\resources\\petimg\\";
                    //前にUUIDを付けるのは、名前の重複を防ぐためです。
                    //前面拼接uiid是为了防止名字重复，获取文件的后缀名，不使用原文件名是防止文件名格式导致无法显示
                    String filename = createUUID.getUUID() + files[i].getOriginalFilename().substring(files[i].getOriginalFilename().lastIndexOf("."));
                    //ファイルオブジェクトを作成し、ファイルの保存パスを設定する
                    //创建文件对象，设置文件保存路径
                    File dest = new File(path + filename);
                    //ファイルオブジェクトを実際のファイルに変換する
                    //将文件对象转化为文件
                    files[i].transferTo(dest);
                    photo.setUrl("petimg/" + filename);
                    photoService.addPhoto(photo);
                }
            }
            else {
                photo.setUrl("defaultpetimg/nodata.jpg");
                photoService.addPhoto(photo);
            }
        }
        return "投稿に成功しました";
    }

    //ペットを削除する
    @GetMapping("/deletePet")
    public String deletePet(@RequestParam(name = "uid",required = false) Long uid, @RequestParam(name = "pid",required = false) Long pid){
        Map map = new HashMap();
        if(uid != null){
            map.put("uid",uid);
        }else{
            map.put("pid",pid);
            map.put("postatu",0);
        }
        List<Petorder> petorder = petorderService.querypetorder(map);
        map.remove("postatu");
        if(petorder.size() == 0){
            petService.deletePet(map);
            photoService.deletePhoto(map);
            return "削除に成功しました";
        }else {
            return "このペットはすでに購入されているため、削除できません";
        }
    }

    // ペット情報を更新する（更新宠物信息)
    @PostMapping("/updatePet")
    public String updatePet(@RequestParam(name = "files",required = false) MultipartFile[] files,@RequestParam(name = "ppid",required = false) Long[] ppid, Pet pet) throws IOException {
        Date date = new Date();
        pet.setDate(date);
        Photo photo = new Photo();
        photo.setPid(pet.getPid());
        //写真を削除する(删除图片)
        if(ppid != null) {
            if(ppid.length > 0) {
                Map photomap = new HashMap();
                for(int i=0; i < ppid.length; i++) {
                    photomap.put("ppid",ppid[i]);
                    photoService.deletePhoto(photomap);
                    photomap.remove("ppid");
                }
            }
        }
        //写真を追加する（增加图片）
        if(files != null) {
            if(files.length > 0) {
                for (int i=0;i< files.length;i++){
                    //現在のプロジェクトパスを取得する（获取当前项目路径）
                    String path = System.getProperty("user.dir") + "\\src\\main\\resources\\resources\\petimg\\";
                    //前にUUIDを付けるのは、名前の重複を防ぐためです。
                    //前面拼接uiid是为了防止名字重复，获取文件的后缀名，不使用原文件名是防止文件名格式导致无法显示
                    String filename = createUUID.getUUID() + files[i].getOriginalFilename();
                    //ファイルオブジェクトを作成し、ファイルの保存パスを設定する
                    //创建文件对象，设置文件保存路径
                    File dest = new File(path + filename);
                    //ファイルオブジェクトを実際のファイルに変換する
                    //将文件对象转化为文件
                    files[i].transferTo(dest);
                    photo.setUrl("petimg/" + filename);
                    photoService.addPhoto(photo);
                }
            } else {
                System.out.println("新しい写真は追加されていません");
            }
        }
        //ペットが購入希望の場合、画像がない場合は「データなし」の写真を追加する
        //宠物为求购时，若没有图片，增加无数据图片
        if(pet.getPk() == 3) {
            List<Photo> photos = photoService.queryPhotobypid(pet.getPid());
            if(photos.size() == 0) {
                photo.setUrl("defaultpetimg/nodata.jpg");
                photoService.addPhoto(photo);
            }
        }
        petService.updatePet(pet);
        return "修正完了";
    }

    //IDでペットを検索する（根据id查询宠物）
    @GetMapping("/querypetbyid")
    public Pet queryPetById(Long pid){
        Pet pet = petService.queryPetById(pid);
        return pet;
    }

    // 名前であいまい検索を行う(根据名字模糊查询)
    @GetMapping("/queryPetByName")
    public List<Pet> queryPetByName(String petname){
        List<Pet> pets = petService.queryPetByName(petname);
        return pets;
    }

    // すべてのペットを検索する(查询所有宠物)
    @GetMapping("/queryAllPet")
    public List<Pet> queryAllPet(@RequestParam(name = "uid",required = false)Long uid,@RequestParam(name = "pk",required = false)Integer pk){
        Map map = new HashMap();
        if(uid != null) {
            map.put("uid",uid);
        }
        if (pk != null) {
            map.put("pk",pk);
        }
        List<Pet> pet = petService.queryAllPet(map);
        return pet;
    }

    //ペットのページネーション検索を行う(分页查询宠物)
    @GetMapping("/querypetpage")
    public List<Pet> querypetpage(int page,int count,
                                  @RequestParam(name = "pid",required = false) Integer pid,
                                  @RequestParam(name = "uid",required = false) Integer uid,
                                  @RequestParam(name = "pk",required = false) Integer pk,
                                  @RequestParam(name = "bkid",required = false) String bkid,
                                  @RequestParam(name = "skid",required = false) String skid,
                                  @RequestParam(name = "age",required = false) String age,
                                  @RequestParam(name = "maxprice",required = false) Double maxprice,
                                  @RequestParam(name = "price",required = false) Double price,
                                  @RequestParam(name = "petname",required = false) String petname,
                                  @RequestParam(name = "date",required = false) String date,
                                  @RequestParam(name = "sort",required = false) String sort){
        Map map = new HashMap();
        map.put("min",(page-1)*count);
        map.put("max",count);
        if(pid != null) {
            map.put("pid",pid);
        }
        if(uid != null) {
            map.put("uid",uid);
        }
        if(pk != null) {
            map.put("pk",pk);
        }
        if(skid != null && skid != ""){
            map.put("skid",skid);
        }else if (bkid !=null && bkid != ""){
            map.put("bkid",bkid);
        }
        if (age != null && age != ""){
            map.put("age",age);
        }
        if (maxprice != null){
            map.put("maxprice",maxprice);
        }
        if (price != null){
            map.put("price",price);
        }
        if (petname != null && petname != ""){
            map.put("petname",petname);
        }
        if (date != null && date != ""){
            map.put("date",date);
        }
        if (sort != null && sort != ""){
            map.put("sort",sort);
        }
        List<Pet> pet = petService.queryPetpage(map);
        return pet;
    }

    //ペットの件数を取得する(查询宠物条数)
    @GetMapping("/querypetcount")
    public int querypetcount(@RequestParam(name = "pid",required = false) Integer pid,
                             @RequestParam(name = "uid",required = false) Integer uid,
                             @RequestParam(name = "pk",required = false) Integer pk,
                             @RequestParam(name = "bkid",required = false) String bkid,
                             @RequestParam(name = "skid",required = false) String skid,
                             @RequestParam(name = "age",required = false) String age,
                             @RequestParam(name = "price",required = false) Double price,
                             @RequestParam(name = "date",required = false) String date,
                             @RequestParam(name = "petname",required = false) String petname) {
        Map map = new HashMap();
        if(pid != null) {
            map.put("pid",pid);
        }
        if(uid != null) {
            map.put("uid",uid);
        }
        if (pk != null) {
            map.put("pk", pk);
        }
        if (skid != null && skid != "") {
            map.put("skid", skid);
        } else if (bkid != null && bkid != "") {
            map.put("bkid", bkid);
        }
        if (age != null && age != ""){
            map.put("age",age);
        }
        if (petname != null && petname != ""){
            map.put("petname",petname);
        }
        if (price != null){
            map.put("price",price);
        }
        if (date != null && date != ""){
            map.put("date",date);
        }
        return petService.querypetcount(map);
    }

    //すべてのペットの最高価格を取得する(查询所有宠物的最高价)
    @GetMapping("/queryMaxPrice")
    public String queryMaxPrice(){
        return petService.queryMaxPrice();
    }

    // すべてのペットの年齢を取得する(查询所有宠物年龄)
    @GetMapping("/queryage")
    public List<Map> queryage(){
        return petService.queryage();
    }
}
