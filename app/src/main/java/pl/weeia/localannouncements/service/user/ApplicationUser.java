package pl.weeia.localannouncements.service.user;


import pl.weeia.localannouncements.shared.enums.Gender;

public class ApplicationUser {

	private long id;
	private String username;
	private int age;
	private Gender gender;

	public ApplicationUser(long id, String username, int age, Gender gender) {
		this.id = id;
		this.username = username;
		this.age = age;
		this.gender = gender;
	}

}
