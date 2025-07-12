package com.example.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
//引数なしのコンストラクタを作成する（生成无参的构造函数）
@NoArgsConstructor
//全ての引数を持つコンストラクタを作成する（生成全参的构造函数）
@AllArgsConstructor
public class Address {
    private Long aid;
    private String phone;
    private String address;
    private String username;
    private Long uid;
    private User user;
}
