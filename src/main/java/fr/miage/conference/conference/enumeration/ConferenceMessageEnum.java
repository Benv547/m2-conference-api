package fr.miage.conference.conference.enumeration;

public enum ConferenceMessageEnum {
    CONFERENCE_NOT_FOUND("Conference not found"),
    CONFERENCE_ALREADY_EXISTS("Conference already exists"),
    CONFERENCE_NOT_VALID("Conference not valid"),
    CONFERENCE_NOT_DELETED("Conference not deleted"),
    CONFERENCE_NOT_UPDATED("Conference not updated"),
    CONFERENCE_NOT_CREATED("Conference not created");

    private String message;

    ConferenceMessageEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
