package pl.weeia.localannouncements.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import okhttp3.ResponseBody;

/**
 * Useful class for parsing server error responses
 */
public class ErrorResponse {

	private ResponseBody responseBody;
	private JsonNode responseRootNode;

	public ErrorResponse(ResponseBody responseBody) {
		this.responseBody = responseBody;
	}

	private void parseJsonIfNotParsedYet() {
		if (responseRootNode == null) {
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				responseRootNode = objectMapper.readValue(responseBody.string(), JsonNode.class);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Checks if server response contains information about invalid value sent for given field
	 */
	public boolean hasFieldError(String fieldName, String message) {
		parseJsonIfNotParsedYet();

		if (!responseRootNode.has("fieldErrors")) {
			return false;
		}

		JsonNode jsonNode = responseRootNode.get("fieldErrors");

		for (JsonNode childNode : jsonNode) {
			if (childNode.has("field")
					&& childNode.get("field").textValue().equalsIgnoreCase(fieldName)
					&& childNode.has("message")
					&& childNode.get("message").textValue().equalsIgnoreCase(message)) {
				return true;
			}
		}

		return false;
	}

	public boolean hasError(String error) {
		return hasFieldError(error, null);
	}

	/**
	 * Checks if server response contains given error
	 */
	public boolean hasError(String error, String errorDescription) {
		parseJsonIfNotParsedYet();

		if (!responseRootNode.has("error") || !responseRootNode.has("error_description")) {
			return false;
		} else if (!responseRootNode.get("error").textValue().equalsIgnoreCase(error)) {
			return false;
		}

		return errorDescription == null || responseRootNode.get("error_description").textValue().equalsIgnoreCase(errorDescription);
	}

}
