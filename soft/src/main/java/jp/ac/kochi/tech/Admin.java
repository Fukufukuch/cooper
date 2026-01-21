package jp.ac.kochi.tech;

public class Admin {

	private String userID;
	private String name;
	private String password;
	private boolean login_flag;

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isLogin_flag() {
		return login_flag;
	}

	public void setLogin_flag(boolean login_flag) {
		this.login_flag = login_flag;
	}
}