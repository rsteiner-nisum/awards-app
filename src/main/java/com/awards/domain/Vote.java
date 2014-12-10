package com.awards.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;


@Document(collection = "T_VOTE")
public class Vote extends AbstractAuditingEntity implements Serializable {
    @Id
    private String id;
    private String userId;
    private String categoryId;
    private String nomineeId;

    public Vote(){}

    public Vote(String userId,
                String categoryId,
                String nomineeId){
        this.userId = userId;
        this.categoryId = categoryId;
        this.nomineeId = nomineeId;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getNomineeId() {
        return nomineeId;
    }

    public void setNomineeId(String nomineeId) {
        this.nomineeId = nomineeId;
    }

    @Override
    public String toString() {
        return "Vote{" +
            "id='" + id + '\'' +
            ", categoryId='" + categoryId + '\'' +
            ", nomineeId='" + nomineeId + '\'' +
            ", userId='" + userId + '\'' +
            "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vote vote = (Vote) o;

        if (id != null && id.equals(vote.id) ) return true;

        if (nomineeId != null ? !nomineeId.equals(vote.nomineeId) : vote.nomineeId != null) return false;
        if (userId != null ? !userId.equals(vote.userId) : vote.userId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        return result;
    }
}
