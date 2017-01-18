package ke.co.eaglesafari.items;

import java.util.List;


public class RequestItem
{
	String id,employee_id="",user_id,sub_service_id="",pick_point="",destination="",amount,status,created_at,
			updated_at,rating,review,pickup_lat,pickup_long,destination_lat,destination_long;
	String db_id;

	public String getPickup_lat() {
		return pickup_lat;
	}

	public void setPickup_lat(String pickup_lat) {
		this.pickup_lat = pickup_lat;
	}

	public String getPickup_long() {
		return pickup_long;
	}

	public void setPickup_long(String pickup_long) {
		this.pickup_long = pickup_long;
	}

	public String getDestination_lat() {
		return destination_lat;
	}

	public void setDestination_lat(String destination_lat) {
		this.destination_lat = destination_lat;
	}

	public String getDestination_long() {
		return destination_long;
	}

	public void setDestination_long(String destination_long) {
		this.destination_long = destination_long;
	}

	List<ChargeItem> charges;

	public List<ChargeItem> getCharges() {
		return charges;
	}

	public void setCharges(List<ChargeItem> charges) {
		this.charges = charges;
	}

	public String getDb_id() {
		return db_id;
	}

	public void setDb_id(String db_id) {
		this.db_id = db_id;
	}

	public UserItem getEmployee() {
		return employee;
	}

	public void setEmployee(UserItem employee) {
		this.employee = employee;
	}

	UserItem employee,user;

	public UserItem getUser() {
		return user;
	}

	public void setUser(UserItem user) {
		this.user = user;
	}

	ServiceItem service;


	public ServiceItem getService() {
		return service;
	}

	public void setService(ServiceItem service) {
		this.service = service;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmployee_id() {
		return employee_id;
	}

	public void setEmployee_id(String employee_id) {
		this.employee_id = employee_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getSub_service_id() {
		return sub_service_id;
	}

	public void setSub_service_id(String sub_service_id) {
		this.sub_service_id = sub_service_id;
	}

	public String getPick_point() {
		return pick_point;
	}

	public void setPick_point(String pick_point) {
		this.pick_point = pick_point;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}
}
