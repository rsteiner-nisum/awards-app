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


}
