package pl.weeia.localannouncements.api.user;


import pl.weeia.localannouncements.shared.enums.Gender;

public class RegistrationForm {

	private String login;
	private String password;
	private int age;
	private Gender gender;
	private String email;

	public RegistrationForm withUsername(String username) {
		this.login = username;
		return this;
	}

	public RegistrationForm withPassword(String password) {
		this.password = password;
		return this;
	}

	public RegistrationForm withAge(int age) {
		this.age = age;
		return this;
	}

	public RegistrationForm withGender(Gender gender) {
		this.gender = gender;
		return this;
	}

	public RegistrationForm withEmail(String email) {
		this.email = email;
		return this;
	}
	public String getLogin() {
		return login;
	}

	public String getPassword() {
		return password;
	}

	public int getAge() {
		return age;
	}

	public Gender getGender() {
		return gender;
	}

	public String getEmail() {
		return email;
	}
}
