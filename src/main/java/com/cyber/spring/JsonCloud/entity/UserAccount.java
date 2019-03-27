package com.cyber.spring.JsonCloud.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Table(name="users")
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty("id")
    private Long id;

    @Column(name="login", length = 128, nullable = false, unique = true)
    @JsonProperty("login")
    private String login;

    @Column(name="fullname", nullable = false)
    @JsonProperty("fullname")
    private String fullName = "";

    @Column(name="password", nullable = false)
    @JsonProperty(value = "password", access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
        name = "users_roles",
        joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn( name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;

    @Column(name = "active_status", nullable = false)
    @JsonProperty("status")
    private Integer status = 0;

    public UserAccount() {

    }

    @JsonGetter("roles")
    public Collection<String> getJsonRoles(){
        return getRoles().stream()
                .map(e -> e.getName())
                .collect(Collectors.toList());
    }

    @JsonSetter("roles")
    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    @JsonSetter("password")
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, fullName, password, status);
    }

    @Override
    public boolean equals(Object obj) {

        if (hashCode() != obj.hashCode()) return false;
        if (!(obj instanceof UserAccount)) return false;

        UserAccount u = (UserAccount)obj;

        return u.getId().equals(getId()) &&
                u.getLogin().equals(getLogin()) &&
                u.getFullName().equals(getFullName()) &&
                u.getPassword().equals(getPassword()) &&
                u.getStatus().equals(getStatus());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
