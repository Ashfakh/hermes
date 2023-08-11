package com.ashfakh.hermes.entity;

import javax.persistence.*;

@Entity
@Table(
        name="summary"
)
public class Summary {


    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private Long botId;

    private Long userId;

    @Column(name = "summary", columnDefinition = "VARCHAR(10000)")
    private String summary;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setBotId(Long botId) {
        this.botId = botId;
    }

    public Long getBotId() {
        return botId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getSummary() {
        return summary;
    }
}
