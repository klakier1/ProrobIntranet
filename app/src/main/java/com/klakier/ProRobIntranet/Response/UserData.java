package com.klakier.ProRobIntranet.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserData extends UserDataShort {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("encrypted_password")
    @Expose
    private String encryptedPassword;
    @SerializedName("confirmation_token")
    @Expose
    private String confirmationToken;
    @SerializedName("remember_token")
    @Expose
    private String rememberToken;
    @SerializedName("avatar_updated_at")
    @Expose
    private String avatarUpdatedAt;
    @SerializedName("days_available")
    @Expose
    private Integer daysAvailable;
    @SerializedName("notify")
    @Expose
    private Boolean notify;

    /**
     * No args constructor for use in serialization
     */
    public UserData() {
    }

    /**
     * @param lastName
     * @param phone
     * @param avatarUpdatedAt
     * @param id
     * @param updatedAt
     * @param title
     * @param encryptedPassword
     * @param email
     * @param createdAt
     * @param notify
     * @param avatarFileSize
     * @param active
     * @param role
     * @param avatarFileName
     * @param firstName
     * @param confirmationToken
     * @param daysAvailable
     * @param rememberToken
     * @param avatarContentType
     */
    public UserData(Integer id, String createdAt, String updatedAt, String email, String encryptedPassword, String confirmationToken, String rememberToken, String avatarFileName, String avatarContentType, Integer avatarFileSize, String avatarUpdatedAt, String role, Boolean active, String firstName, String lastName, String title, String phone, Integer daysAvailable, Boolean notify) {
        super(email,
                avatarFileName,
                avatarContentType,
                avatarFileSize,
                role,
                active,
                firstName,
                lastName,
                title,
                phone);

        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.encryptedPassword = encryptedPassword;
        this.confirmationToken = confirmationToken;
        this.rememberToken = rememberToken;
        this.avatarUpdatedAt = avatarUpdatedAt;
        this.daysAvailable = daysAvailable;
        this.notify = notify;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public String getConfirmationToken() {
        return confirmationToken;
    }

    public void setConfirmationToken(String confirmationToken) {
        this.confirmationToken = confirmationToken;
    }

    public String getRememberToken() {
        return rememberToken;
    }

    public void setRememberToken(String rememberToken) {
        this.rememberToken = rememberToken;
    }

    public String getAvatarUpdatedAt() {
        return avatarUpdatedAt;
    }

    public void setAvatarUpdatedAt(String avatarUpdatedAt) {
        this.avatarUpdatedAt = avatarUpdatedAt;
    }

    public Integer getDaysAvailable() {
        return daysAvailable;
    }

    public void setDaysAvailable(Integer daysAvailable) {
        this.daysAvailable = daysAvailable;
    }

    public Boolean getNotify() {
        return notify;
    }

    public void setNotify(Boolean notify) {
        this.notify = notify;
    }

}