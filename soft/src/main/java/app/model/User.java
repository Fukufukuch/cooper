package app.model;

import java.sql.Date;

public class User {
    private String userID;
    private String username;
    private String usertype; // bit(1)を文字列として受ける場合もあるので String にしてる
    private String password;
    private Date dateOfBirth;
    private String phoneNumber;
    private String email;
    private Integer totalWorking;
    private Integer tag;
    private Integer position;
    private String workPlace;

    public String getUserID() { return userID; }
    public void setUserID(String userID) { this.userID = userID; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getUsertype() { return usertype; }
    public void setUsertype(String usertype) { this.usertype = usertype; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Date getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(Date dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Integer getTotalWorking() { return totalWorking; }
    public void setTotalWorking(Integer totalWorking) { this.totalWorking = totalWorking; }

    public Integer getTag() { return tag; }
    public void setTag(Integer tag) { this.tag = tag; }

    public Integer getPosition() { return position; }
    public void setPosition(Integer position) { this.position = position; }

    public String getWorkPlace() { return workPlace; }
    public void setWorkPlace(String workPlace) { this.workPlace = workPlace; }
}
