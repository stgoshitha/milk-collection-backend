package com.project.milkcollection.common.constants;

/**
 * Regular expression patterns used for input validation.
 */
public final class RegexConstants {

    private RegexConstants() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated.");
    }

    /**
     * Username
     * 4-30 characters
     * Letters, numbers, underscore and dot
     */
    public static final String USERNAME =
            "^[a-zA-Z0-9._]{4,30}$";

    /**
     * Email
     */
    public static final String EMAIL =
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    /**
     * Password
     *
     * Minimum 8 characters
     * At least:
     * - 1 uppercase
     * - 1 lowercase
     * - 1 number
     * - 1 special character
     */
    public static final String PASSWORD =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

    /**
     * Sri Lankan Mobile Number
     *
     * Examples:
     * 0712345678
     * 0771234567
     */
    public static final String MOBILE_NUMBER =
            "^07\\d{8}$";

    /**
     * Sri Lankan Land Phone
     */
    public static final String LAND_PHONE =
            "^0\\d{9}$";

    /**
     * Sri Lankan NIC
     *
     * Old :
     * 123456789V
     *
     * New :
     * 200012345678
     */
    public static final String NIC =
            "^(\\d{9}[VvXx]|\\d{12})$";

    /**
     * Only letters and spaces
     */
    public static final String NAME =
            "^[A-Za-z ]{2,100}$";

    /**
     * Alpha Numeric
     */
    public static final String ALPHA_NUMERIC =
            "^[a-zA-Z0-9]+$";

    /**
     * Decimal Number
     */
    public static final String DECIMAL =
            "^\\d+(\\.\\d{1,2})?$";

    /**
     * Role name
     *
     */
    public static final String ROLE_NAME =
            "^[A-Za-z][A-Za-z0-9_ ]{1,49}$";

    /**
     * Role code
     *
     */
    public static final String ROLE_CODE =
            "^ROLE_[A-Z0-9_]{2,47}$";

    /**
     * System module code
     *
     */
    public static final String SYSTEM_MODULE_NAME =
            "^[A-Za-z][A-Za-z0-9 ]*$";

    /**
     * System module icon
     *
     */
    public static final String SYSTEM_MODULE_ICON =
            "^[A-Za-z][A-Za-z0-9]*$";

    /**
     * System module icon
     *
     */
    public static final String PERMISSION_NAME =
            "^[A-Z]+(?:_[A-Z]+)*$";
}