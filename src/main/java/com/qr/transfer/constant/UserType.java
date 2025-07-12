package com.qr.transfer.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@SuppressWarnings("AlibabaClassMustHaveAuthor")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum UserType {

    /**
     * 代理
     */
    PROXY("proxy"),

    /**
     * 客户
     */
    CUSTOMER("customer"),

    /**
     * 管理员
     */
    ADMIN("admin"),
    ;


    private String userType;


}
