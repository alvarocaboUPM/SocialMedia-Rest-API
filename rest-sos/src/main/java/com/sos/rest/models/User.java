package com.sos.rest.models;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import java.util.Set;

@XmlRootElement(name="users")
public class User {

@XmlElement(name = "userId")
private Long userId;

@XmlElement(name = "name")
private String name;

@XmlElement(name = "email")
private String email;

@XmlElement(name = "age")
private Integer age;

@XmlElementWrapper(name = "friends")
@XmlElement(name = "friend")
private Set<User> friends;

public User() {}

public User(String name, String email, Integer age) {
    this.name = name;
    this.email = email;
    this.age = age;
}

// Getters and setters

public Long getUserId() {
    return userId;
}

public void setUserId(Long userId) {
    this.userId = userId;
}

public String getName() {
    return name;
}

public void setName(String name) {
    this.name = name;
}

public String getEmail() {
    return email;
}

public void setEmail(String email) {
    this.email = email;
}

public Integer getAge() {
    return age;
}

public void setAge(Integer age) {
    this.age = age;
}

public Set<User> getFriends() {
    return friends;
}

public void setFriends(Set<User> friends) {
    this.friends = friends;
}

}