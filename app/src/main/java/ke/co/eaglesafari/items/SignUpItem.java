package ke.co.eaglesafari.items;

public class SignUpItem
	{
	String name;
		String phone;
		String dob;
		String email;
		String password;

		public String getConfirm_password() {
			return confirm_password;
		}

		public void setConfirm_password(String confirm_password) {
			this.confirm_password = confirm_password;
		}

		String confirm_password;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}

		public String getDob() {
			return dob;
		}

		public void setDob(String dob) {
			this.dob = dob;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public BooleanStringItem isEmpty()
		{
			if(name.isEmpty())
			{
				return new BooleanStringItem().setaBoolean(false).setMessage("Name Required");
			}
			if(email.isEmpty())
			{
				return new BooleanStringItem().setaBoolean(false).setMessage("Email Required");
			}
			if(phone.isEmpty())
			{
				return new BooleanStringItem().setaBoolean(false).setMessage("Phone Required");
			}
			if(password.length()<8)
			{
				return new BooleanStringItem().setaBoolean(false).setMessage("Password cannot be less than 8 characters");
			}
			if(password.isEmpty())
				{
					return new BooleanStringItem().setaBoolean(false).setMessage("Password Required");
				}
			if(confirm_password.isEmpty())
			{
				return new BooleanStringItem().setaBoolean(false).setMessage("Confirm Password Required");
			}
			if(!password.equals(confirm_password))
			{
				return new BooleanStringItem().setaBoolean(false).setMessage("Confirm Password and Password do not match");
			}




			return new BooleanStringItem().setaBoolean(true).setMessage("Ok");
		}
	}
