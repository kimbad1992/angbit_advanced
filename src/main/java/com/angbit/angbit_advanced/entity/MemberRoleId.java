package com.angbit.angbit_advanced.entity;// MemberRoleId.java
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
public class MemberRoleId implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long memberId;
    private String role;

    public MemberRoleId() {}

    public MemberRoleId(Long memberId, String role) {
        this.memberId = memberId;
        this.role = role;
    }
}
