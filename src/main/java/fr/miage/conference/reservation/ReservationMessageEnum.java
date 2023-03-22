package fr.miage.conference.reservation;

public enum ReservationMessageEnum {
    RESERVATION_NOT_FOUND("Reservation not found"),
    RESERVATION_NOT_FOUND_BY_CONFERENCE_ID("Reservation not found for conference with id: "),
    RESERVATION_NOT_FOUND_BY_SESSION_ID("Reservation not found for session with id: "),
    RESERVATION_NOT_FOUND_BY_USER_ID("Reservation not found for user with id: "),
    RESERVATION_IS_LOCKED("Reservation is locked"),
    RESERVATION_BANK_REFUSED("Reservation bank refused"),
    RESERVATION_IS_FULL("Reservation is full");

    private String message;

    ReservationMessageEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
