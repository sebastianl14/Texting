package com.example.texting.common.pojo;

import com.google.firebase.database.Exclude;

/**
 * Creado por Sebastian Londoño Benitez
 * Email: sebastianl14@hotmail.com
 * Fecha 31/08/2020.
 * Derechos Reservados 2020
 */
public class Message {

    private String msg;
    private String sender;
    private String photoUrl;
    @Exclude
    private boolean sendByMe;
    @Exclude
    private String uid;
    @Exclude
    private boolean loaded;

    public Message() {
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    @Exclude
    public boolean isSendByMe() {
        return sendByMe;
    }
    @Exclude
    public void setSendByMe(boolean sendByMe) {
        this.sendByMe = sendByMe;
    }
    @Exclude
    public String getUid() {
        return uid;
    }
    @Exclude
    public void setUid(String uid) {
        this.uid = uid;
    }
    @Exclude
    public boolean isLoaded() {
        return loaded;
    }
    @Exclude
    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        return uid != null ? uid.equals(message.uid) : message.uid == null;
    }

    @Override
    public int hashCode() {
        return uid != null ? uid.hashCode() : 0;
    }
}
