package com.klakier.ProRobIntranet.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserDataShort {

    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("avatar_file_name")
    @Expose
    private Object avatarFileName;
    @SerializedName("avatar_content_type")
    @Expose
    private Object avatarContentType;
    @SerializedName("avatar_file_size")
    @Expose
    private Object avatarFileSize;
    @SerializedName("role")
    @Expose
    private String role;
    @SerializedName("active")
    @Expose
    private Boolean active;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("phone")
    @Expose
    private String phone;

    /**
     * No args constructor for use in serialization
     *
     */
    public UserDataShort() {
    }

    /**
     *
     * @param lastName
     * @param phone
     * @param title
     * @param email
     * @param avatarFileSize
     * @param active
     * @param role
     * @param avatarFileName
     * @param firstName
     * @param avatarContentType
     */
    public UserDataShort(String email, Object avatarFileName, Object avatarContentType, Object avatarFileSize, String role, Boolean active, String firstName, String lastName, String title, String phone) {
        super();
        this.email = email;
        this.avatarFileName = avatarFileName;
        this.avatarContentType = avatarContentType;
        this.avatarFileSize = avatarFileSize;
        this.role = role;
        this.active = active;
        this.firstName = firstName;
        this.lastName = lastName;
        this.title = title;
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Object getAvatarFileName() {
        return avatarFileName;
    }

    public void setAvatarFileName(Object avatarFileName) {
        this.avatarFileName = avatarFileName;
    }

    public Object getAvatarContentType() {
        return avatarContentType;
    }

    public void setAvatarContentType(Object avatarContentType) {
        this.avatarContentType = avatarContentType;
    }

    public Object getAvatarFileSize() {
        return avatarFileSize;
    }

    public void setAvatarFileSize(Object avatarFileSize) {
        this.avatarFileSize = avatarFileSize;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}