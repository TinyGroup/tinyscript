
package org.tinygroup.tinyscript.csv;

public enum QuoteMode {

    /**
     * Quotes all fields.
     */
    ALL,

    /**
     * Quotes fields which contain special characters such as a delimiter, quote character or any of the characters in
     * line separator.
     */
    MINIMAL,

    /**
     * Quotes all non-numeric fields.
     */
    NON_NUMERIC,

    /**
     * Never quotes fields. When the delimiter occurs in data, it is preceded by the current escape character. If the
     * escape character is not set, printing will throw an exception if any characters that require escaping are
     * encountered.
     */
    NONE
}
