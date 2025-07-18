package com.example.controller;

import com.example.domain.Pet;
import com.example.domain.Petorder;
import com.example.service.PetService;
import com.example.service.PetorderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/petorder")
public class PetorderController {

    @Autowired
    private PetorderService petorderService;

    @Autowired
    private PetService petService;

    @PostMapping("/addpetorder")
    public Map addpetorder(Petorder petorder){
        Pet spet = petService.queryPetById(petorder.getPid());
        Map map = new HashMap();
        if(spet == null) {
            map.put("flag",0);
            map.put("msg","販売者がこのペットを削除しました");
            return map;
        }else {
            if(spet.getPk() == 0){
                Date date = new Date();
                petorder.setDate(date);
                Pet pet = new Pet();
                pet.setPid(petorder.getPid());
                pet.setPk(1);
                petService.updatePet(pet);
                petorderService.addpetorder(petorder);
                map.put("flag",1);
                map.put("msg","注文の送信に成功しました");
                return map;
            }else {
                map.put("flag",0);
                map.put("msg","このペットはすでに購入されています");
                return map;
            }
        }
    }

    @GetMapping("/deletepetorder")
    public String deletepetorder(@RequestParam(name = "poid",required = false)Long poid,@RequestParam(name = "uid",required = false)Long uid){
        Map map = new HashMap();
        if(poid != null){
            map.put("poid",poid);
        }
        if (uid != null){
            map.put("uid",uid);
        }
        petorderService.deletepetorder(map);
        return "削除に成功しました";
    }

    @PostMapping("/updatepetorder")
    public String updatepetorder(Petorder petorder,@RequestParam(name = "pk",required = false)Integer pk){
        Date date = new Date();
        petorder.setDate(date);
        petorderService.updatepetorder(petorder);
        if(pk != null) {
            Pet pet = new Pet();
            pet.setPid(petorder.getPid());
            pet.setPk(pk);
            petService.updatePet(pet);
        }
        if(petorder.getPostatu() == 3){
            return "注文は修正中で、販売者の確認を待っています";
        }else if(petorder.getPostatu() == 0){
            return "注文は正常に修正されました";
        }else if(petorder.getPostatu() == 2){
            return "注文はキャンセルされました";
        }else if(petorder.getPostatu() == 4){
            return " 注文をキャンセル中です。販売者の確認をお待ちください";
        }else {
            return "注文完了";
        }
    }

    @GetMapping("/querypetorder")
    public List<Petorder> querypetorder(
                            @RequestParam(name = "uid",required = false) Long uid,
                            @RequestParam(name = "pid",required = false) Long pid,
                            @RequestParam(name = "poid",required = false) Long poid,
                            @RequestParam(name = "recipientname",required = false) String recipientname,
                            @RequestParam(name = "address",required = false) String address,
                            @RequestParam(name = "phone",required = false) String phone,
                            @RequestParam(name = "postatu",required = false) Integer postatu,
                            @RequestParam(name = "date",required = false) String date){
        Map map = new HashMap();
        if(uid != null) {
            map.put("uid",uid);
        }
        if(pid != null) {
            map.put("pid",pid);
        }
        if(poid != null) {
            map.put("poid",poid);
        }
        if(recipientname != null && recipientname != "") {
            map.put("recipientname",recipientname);
        }
        if(address != null && address != "") {
            map.put("address",address);
        }
        if(phone != null && phone != "") {
            map.put("phone",phone);
        }
        if(postatu != null) {
            map.put("postatu",postatu);
        }
        if(date != null &&  date != "") {
            map.put("date",date);
        }
        return petorderService.querypetorder(map);
    }

    @GetMapping("/querypetorderpage")
    public Map querypetorderpage(@RequestParam(name = "page",required = false)Integer page,
                             @RequestParam(name = "count",required = false)Integer count,
                             @RequestParam(name = "uid",required = false) Long uid,
                             @RequestParam(name = "pid",required = false) Long pid,
                             @RequestParam(name = "poid",required = false) Long poid,
                             @RequestParam(name = "recipientname",required = false) String recipientname,
                             @RequestParam(name = "address",required = false) String address,
                             @RequestParam(name = "phone",required = false) String phone,
                             @RequestParam(name = "postatu",required = false) Integer postatu,
                             @RequestParam(name = "date",required = false) String date){
        Map map = new HashMap();
        if(page != null && count != null){
            map.put("min",(page-1)*count);
            map.put("max",count);
        }
        if(uid != null) {
            map.put("uid",uid);
        }
        if(pid != null) {
            map.put("pid",pid);
        }
        if(poid != null) {
            map.put("poid",poid);
        }
        if(recipientname != null && recipientname != "") {
            map.put("recipientname",recipientname);
        }
        if(address != null && address != "") {
            map.put("address",address);
        }
        if(phone != null && phone != "") {
            map.put("phone",phone);
        }
        if(postatu != null) {
            map.put("postatu",postatu);
        }
        if(date != null &&  date != "") {
            map.put("date",date);
        }
        Map map1 = new HashMap();
        map1.put("total",petorderService.countpetorder(map));
        map1.put("petorder",petorderService.querypetorderpage(map));
        return map1;
    }
}
