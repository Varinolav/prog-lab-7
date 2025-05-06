package ru.varino.server.managers.utility;

import ru.varino.common.communication.RequestEntity;
import ru.varino.common.communication.ResponseEntity;

public class ClientData {
    private RequestEntity requestEntity;
    private ResponseEntity responseEntity;


    public RequestEntity requestEntity() {
        return requestEntity;
    }

    public ClientData setRequestEntity(RequestEntity requestEntity) {
        this.requestEntity = requestEntity;
        return this;
    }

    public ResponseEntity responseEntity() {
        return responseEntity;
    }

    public ClientData setResponseEntity(ResponseEntity responseEntity) {
        this.responseEntity = responseEntity;
        return this;
    }
}