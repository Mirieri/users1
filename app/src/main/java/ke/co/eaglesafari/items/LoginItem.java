package ke.co.eaglesafari.items;

public class LoginItem
{
	String	email, password, gcm;

	public String getGcm()
	{
		return gcm;
	}

	public void setGcm(String gcm)
	{
		this.gcm = gcm;
	}

	public String getEmail()
	{
		return email;
	}



	public String getPassword()
	{
		return password;
	}


	public LoginItem setEmail(String email)
	{
		this.email=email;
		return this;
	}
	public LoginItem setPassword(String password)
	{
		this.password=password;
		return  this;
	}


}

