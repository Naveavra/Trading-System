package service.security;

import service.payment.PaymentAdapter;

public class ProxySecurity implements SecurityAdapter {

    private SecurityAdapter real = null;

    //TODO: need to add a list of reals and check when to change to each one
    public void setRealSecurity(SecurityAdapter securityAdapter){
        if(real == null)
            real = securityAdapter;
    }

    @Override
    public void checkUser() {

    }
}
