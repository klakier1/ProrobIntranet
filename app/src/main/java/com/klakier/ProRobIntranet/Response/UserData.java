package com.klakier.ProRobIntranet.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserData {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("encrypted_password")
    @Expose
    private String encryptedPassword;
    @SerializedName("confirmation_token")
    @Expose
    private String confirmationToken;
    @SerializedName("remember_token")
    @Expose
    private String rememberToken;
    @SerializedName("avatar_file_name")
    @Expose
    private String avatarFileName;
    @SerializedName("avatar_content_type")
    @Expose
    private String avatarContentType;
    @SerializedName("avatar_file_size")
    @Expose
    private Integer avatarFileSize;
    @SerializedName("avatar_updated_at")
    @Expose
    private String avatarUpdatedAt;
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
    @SerializedName("days_available")
    @Expose
    private Integer daysAvailable;
    @SerializedName("notify")
    @Expose
    private Boolean notify;

    /**
     * No args constructor for use in serialization
     *
     */
    public UserData() {
    }

    /**
     *
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
        super();
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.email = email;
        this.encryptedPassword = encryptedPassword;
        this.confirmationToken = confirmationToken;
        this.rememberToken = rememberToken;
        this.avatarFileName = avatarFileName;
        this.avatarContentType = avatarContentType;
        this.avatarFileSize = avatarFileSize;
        this.avatarUpdatedAt = avatarUpdatedAt;
        this.role = role;
        this.active = active;
        this.firstName = firstName;
        this.lastName = lastName;
        this.title = title;
        this.phone = phone;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getAvatarFileName() {
        return avatarFileName;
    }

    public void setAvatarFileName(String avatarFileName) {
        this.avatarFileName = avatarFileName;
    }

    public String getAvatarContentType() {
        return avatarContentType;
    }

    public void setAvatarContentType(String avatarContentType) {
        this.avatarContentType = avatarContentType;
    }

    public Integer getAvatarFileSize() {
        return avatarFileSize;
    }

    public void setAvatarFileSize(Integer avatarFileSize) {
        this.avatarFileSize = avatarFileSize;
    }

    public String getAvatarUpdatedAt() {
        return avatarUpdatedAt;
    }

    public void setAvatarUpdatedAt(String avatarUpdatedAt) {
        this.avatarUpdatedAt = avatarUpdatedAt;
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