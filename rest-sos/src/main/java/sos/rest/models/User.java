    package sos.rest.models;


    import javax.xml.bind.annotation.XmlElement;
    import javax.xml.bind.annotation.XmlElementWrapper;
    import javax.xml.bind.annotation.XmlRootElement;

    import java.util.Set;

    @XmlRootElement(name="user")
    public class User {


    private Long userId;
        @XmlElement(name = "userId")
        public Long getUserId() {
            return userId;
        }
    public void setUserId(Long userId) {this.userId = userId;}


    private String name;
        @XmlElement(name = "name")
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }




    private String email;
        @XmlElement(name = "email")
        public String getEmail() {
            return email;
        }
        public void setEmail(String email) {
            this.email = email;
        }


    private Integer age;
        @XmlElement(name = "age")
        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

    private Set<User> friends;
        @XmlElementWrapper(name = "friends")
        @XmlElement(name = "friend")
        public Set<User> getFriends() {
            return friends;
        }

        public void setFriends(Set<User> friends) {
            this.friends = friends;
        }

    public User() {}

    public User(String name, String email, Integer age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }

    }