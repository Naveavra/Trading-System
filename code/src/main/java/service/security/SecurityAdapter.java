package service.security;

//TODO: make payment and supplier the same way as security
public abstract class SecurityAdapter {

    private SecurityAdapter securityAdapter;
    public SecurityAdapter(SecurityAdapter securityAdapter){
        this.securityAdapter = securityAdapter;
    }
}
