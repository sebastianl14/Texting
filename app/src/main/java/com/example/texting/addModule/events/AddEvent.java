package com.example.texting.addModule.events;

/**
 * Creado por Sebastian Londoño Benitez
 * Email: sebastianl14@hotmail.com
 * Fecha 22/08/2020.
 * Derechos Reservados 2020
 */
public class AddEvent {

    public static final int SEND_REQUEST_SUCCESS = 0;
    public static final int ERROR_SERVER = 100;

    private int typeEvent;

    public AddEvent() {
    }

    public int getTypeEvent() {
        return typeEvent;
    }

    public void setTypeEvent(int typeEvent) {
        this.typeEvent = typeEvent;
    }
}
