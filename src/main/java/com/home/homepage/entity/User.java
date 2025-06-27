package com.home.homepage.entity;

import com.home.homepage.define.UserRole;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Calendar;
import java.util.Collection;
import java.util.List;

/**
 * 用户实体类
 *
 * @author Jachin
 * @since 25-06-16 10:30
 */
@Data
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role = UserRole.User.name();

    @Column(nullable = false)
    private Boolean enabled = true;

    private String avatar;

    @CreationTimestamp
    @ToString.Exclude
    private Calendar createTime;
    @UpdateTimestamp
    @ToString.Exclude
    private Calendar updateTime;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
