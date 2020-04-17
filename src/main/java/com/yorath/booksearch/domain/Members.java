package com.yorath.booksearch.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Members extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(length = 12)
    private String userId;

    /**
     * SHA-256 + salt(난수)로 암호화된 비빌번호
     */
    @Column(length = 256)
    private String password;

    @Builder
    public Members(String userId, String password, String salt) {
        this.userId = userId;
        this.password = password;
    }
}
