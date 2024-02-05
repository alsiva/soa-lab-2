package org.ifmo.soalab2;

import jakarta.xml.bind.annotation.*;

import java.io.Serializable;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ApiResponseMessage {

    String details;

    public ApiResponseMessage() {
    }

    public ApiResponseMessage(String details){
        this.details = details;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "ApiResponseMessage{" +
                "details='" + details + '\'' +
                '}';
    }
}
