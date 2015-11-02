package itb5.atm;

import java.rmi.RemoteException;

import itb5.atm.IATM;
import itb5.atm.IATMFactory;

public class ATMFactoryImpl implements IATMFactory {
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