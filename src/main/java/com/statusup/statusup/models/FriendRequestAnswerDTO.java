package com.statusup.statusup.models;

public class FriendRequestAnswerDTO {

    private String friendRequestId;
    private Boolean isAccepted;

    public String getFriendRequestId() {
        return friendRequestId;
    }

    public void setFriendRequestId(String friendRequestId) {
        this.friendRequestId = friendRequestId;
    }

    public Boolean getIsAccepted() {
        return isAccepted;
    }

    public void setIsAccepted(Boolean isAccepted) {
        this.isAccepted = isAccepted;
    }

}
