package sos.rest.models;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import java.time.LocalDateTime;

@XmlRootElement(name="messages")
public class Message {


    @XmlElement(name = "messageId")
    private Long messageId;


    @XmlElement(name = "author")
    private User author;


    @XmlElement(name = "receiver")
    private User receiver;

    @XmlElement(name = "message")
    private String message;

    @XmlElement(name = "time")
    private LocalDateTime time;

    public Message() {}

    public Message(User author, User receiver, String message, LocalDateTime time) {
        this.author = author;
        this.receiver = receiver;
        this.message = message;
        this.time = time;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}