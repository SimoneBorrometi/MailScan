package org.dmi.mailscanner;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Email {

    private final String from;

    private final String to;
    
    private final String message;

    public Email() {
        this.to = null;
        this.message = null;
        this.from = null;
    }

    public Email(String from, String to, String message) {
        this.from = from;
        this.to = to;
        this.message = message;
    }


    public String getFrom() {
        return this.from;
    }


    public String getTo() {
        return this.to;
    }


    public String getMessage() {
        return this.message;
    }


    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Email)) {
            return false;
        }
        Email email = (Email) o;
        return Objects.equals(from, email.from) && Objects.equals(to, email.to) && Objects.equals(message, email.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, message);
    }

    @Override
    public String toString() {
        return "{" +
            " from ='" + getFrom() + "'" +
            ", to ='" + getTo() + "'" +
            ", message='" + getMessage() + "'" +
            "}";
    }
    
}
