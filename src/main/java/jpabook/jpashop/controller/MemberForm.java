package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.NotFound;

@Getter @Setter
public class MemberForm {
    private String name;

    private String city;
    private String street;
    private String zipcode;
}
