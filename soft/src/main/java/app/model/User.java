package app.model;

import java.time.LocalDate;

public class User {
    private String userID;
    private String username;
    private boolean usertype; // bit(1) -> getBoolean でOK（true=1, false=0）
    private String email;
    private String phoneNumber;
    private String workPlace;

    private int tag;
    private int position;

    private LocalDate dateOfBirth;

    public String getUserID() { return userID; }
    public void setUserID(String userID) { this.userID = userID; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public boolean isUsertype() { return usertype; }
    public void setUsertype(boolean usertype) { this.usertype = usertype; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getWorkPlace() { return workPlace; }
    public void setWorkPlace(String workPlace) { this.workPlace = workPlace; }

    public int getTag() { return tag; }
    public void setTag(int tag) { this.tag = tag; }

    public int getPosition() { return position; }
    public void setPosition(int position) { this.position = position; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
}
