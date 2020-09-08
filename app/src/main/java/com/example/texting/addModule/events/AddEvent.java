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
    public static final int ERROR_EXIST = 101;

    private int typeEvent;
    private int resMsg;

    public AddEvent() {
    }

    public int getTypeEvent() {
        return typeEvent;
    }

    public void setTypeEvent(int typeEvent) {
        this.typeEvent = typeEvent;
    }

    public int getResMsg() {
        return resMsg;
    }

    public void setResMsg(int resMsg) {
        this.resMsg = resMsg;
    }
}
