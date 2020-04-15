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
     * SHA-256 + salt(난수로 암호화된 비빌번호
     */
    @Column(length = 256)
    private String password;

    /**
     * 비밀번호 암호화에 사용되는 난수, 로그인시 필요함
     */
    @Column(length = 256)
    private String salt;

    @Builder
    public Members(String userId, String password, String salt) {
        this.userId = userId;
        this.password = password;
        this.salt = salt;
    }
}
