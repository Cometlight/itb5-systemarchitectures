package itb5.atm;

import itb5.atm.IATM;
import itb5.atm.IATMFactory;

public class ATMFactoryImpl implements IATMFactory {
    public IATM createATM() {
        return new ATMImpl();
    }
}