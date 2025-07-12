package com.example.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//import javax.persistence.*;
import java.util.Date;
import java.util.List;

//@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pet {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pid;//ペットID（宠物id）
    private String petname;//ペットのタイトル（宠物标题）
    private String sex;//ペットの性別（宠物性别）
    private Double price;//ペットの価格（宠物价格）
    private Integer age;//ペットの年齢（宠物年龄）
    private String description;//ペットの説明（宠物描述）
    private Integer pk;//ペットの販売ステータス（0：販売中、1：販売済み、2：販売完了、3：購入希望を募集中）
    // 宠物发售状态（0-出售中，1-已出售，2-已完成出售，3-发布求购中）
    private Long uid;//対応する購入者または販売者のUID
    private Long bkid;//種類に対応するBKID
    private Long skid;//品種に対応するSKID
    private Date date;//登録日

//    @ManyToOne
//    @JoinColumn(name = "uid")
    private User user;//ペットを投稿した購入者または販売者（发布宠物的买家或卖家）

//    @ManyToOne
//    @JoinColumn(name = "bkid")
    private BKind bkind;//ペットの種類（宠物所属的类型）

//    @ManyToOne
//    @JoinColumn(name = "skid")
    private SKind skind;//ペットの品種（宠物所属的品种）

//    @OneToMany(mappedBy = "pet")
    private List<Photo> photo;//ペットの写真（宠物图片,最多5张）

//    @OneToMany(mappedBy = "pet")
    private List<Petorder> petorder;
}
