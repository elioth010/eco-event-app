package com.bytesw.consultadecuentas.eis.bo;

/**
 * @author elioth010
 */
public class UserToken extends BaseBO<UserToken> {

    /**
     *
     */
    private static final long serialVersionUID = -2657522461715497809L;
    private Long id;
    private String username;
    private String token;
    private String tokenType;
    private String loginDate;

    public UserToken() {
        super();
    }

    public UserToken(Integer id, String username, String token, String tokenType, String loginDate) {
        this.id = id.longValue();
        this.username = username;
        this.token = token;
        this.tokenType = tokenType;
        this.loginDate = loginDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getLastLogin() {
        return loginDate;
    }

    public void setLastLogin(String lastLogin) {
        this.loginDate = lastLogin;
    }

    @Override
    public String toString() {
        return "UserToken [id=" + super.getId() + ", userid=" + username
                + ", token=" + token + ", tokenType=" + tokenType
                + ", lastLogin=" + loginDate + "]";
    }

}