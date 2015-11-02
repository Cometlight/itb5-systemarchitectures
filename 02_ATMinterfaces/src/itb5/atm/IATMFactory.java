package itb5.atm;

import java.rmi.Remote;

import itb5.atm.IATM;

public interface IATMFactory extends Remote {
    public IATM createATM();
}