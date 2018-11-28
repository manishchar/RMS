package alina.com.rms.model;

/**
 * Created by HP on 02-01-2018.
 */

public class LoginDto {

    private String email;
    private String password;

    public void setEmail(String email)
    {
        this.email=email;
    }

    public String getEmail()
    {
        return  email;
    }


    public void setPassword(String password)
    {
        this.password=password;
    }

    public String getPassword()
    {
        return  password;
    }
}
