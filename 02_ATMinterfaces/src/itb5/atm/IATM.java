package itb5.atm;

import java.rmi.RemoteException;

public interface IATM {
    public void deposit(int accountNo, float amount) throws RemoteException;
    public void withdraw(int accountNo, float amount) throws RemoteException;
    public float getBalance(int accountNo) throws RemoteException;
}
