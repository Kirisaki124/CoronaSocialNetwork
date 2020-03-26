package hava.coronasocialnetwork.database.operator

enum class LoginStatus {
    NO_ACCOUNT_FOUND, INVALID_PASSWORD, OK
}

enum class RegisterStatus {
    EMAIL_ALREADY_EXISTED, WEAK_PASSWORD, OK, WRONG_EMAIL_FORMAT
}

enum class UpdateStatus {
    OK, FAILED
}