package pl.weeia.localannouncements.api.user;


import com.fasterxml.jackson.annotation.JsonProperty;

import pl.weeia.localannouncements.shared.enums.Gender;

class IdentityResponse {

	@JsonProperty("id")
	private int id;
	@JsonProperty("username")
	private String username;
	@JsonProperty("age")
	private int age;
	@JsonProperty("gender")
	private Gender gender;

	int getId() {
		return id;
	}

	String getUsername() {
		return username;
	}

	int getAge() {
		return age;
	}

	Gender getGender() {
		return gender;
	}

}
