package itb5.atm;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import itb5.atm.IATM;
import itb5.atm.IATMFactory;

public class ATMFactoryImpl extends UnicastRemoteObject implements IATMFactory, Serializable {
	protected ATMFactoryImpl() throws RemoteException {
		super();
	}

	private static final long serialVersionUID = 1L;

	@Override
    public IATM createATM() {
        try {
			return new ATMImpl();
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
    }
}