package com.ashfakh.hermes.entity;


import javax.persistence.*;

@Entity
@Table(
        name = "Users",
        uniqueConstraints =
        @UniqueConstraint(columnNames = {"channel", "channelId"})
)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String channel;

    private String channelId;

    private Long userCreationTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Long getUserCreationTime() {
        return userCreationTime;
    }

    public void setUserCreationTime(Long userCreationTime) {
        this.userCreationTime = userCreationTime;
    }
}
