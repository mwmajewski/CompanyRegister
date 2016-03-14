package pl.majek.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Created by majewskm on 2016-02-27.
 */
@Entity
public class Company {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	@Size(min = 1)
	private String name;

	@NotNull
	@Size(min = 1)
	private String address;

	@NotNull
	@Size(min = 1)
	private String city;

	@NotNull
	@Size(min = 1)
	private String country;

	@Pattern(regexp = "\\b[A-Za-z0-9\\._%\\+\\-]+@[A-Za-z0-9\\.\\-]+\\.[A-Za-z]{2,}\\b")
	private String email;

	@Pattern(regexp = "\\+?[0-9 ]{4,}")
	private String phoneNumber;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<BeneficialOwner> beneficialOwners;

	public Company(Long id, String name, String address, String city, String country, String email, String phoneNumber) {
		this.id = id;
		this.name = name;
		this.address = address;
		this.city = city;
		this.country = country;
		this.email = email;
		this.phoneNumber = phoneNumber;
	}

	public Company(Long id, String name, String address, String city, String country, String email, String phoneNumber, List<BeneficialOwner> beneficialOwners) {
		this.id = id;
		this.name = name;
		this.address = address;
		this.city = city;
		this.country = country;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.beneficialOwners = beneficialOwners;
	}

	public Company() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public List<BeneficialOwner> getBeneficialOwners() {
		return beneficialOwners;
	}

	public void setBeneficialOwners(List<BeneficialOwner> beneficialOwners) {
		this.beneficialOwners = beneficialOwners;
	}

	@Override
	public String toString() {
		return "Company{" +
				"id=" + id +
				", name='" + name + '\'' +
				", address='" + address + '\'' +
				", city='" + city + '\'' +
				", country='" + country + '\'' +
				", email='" + email + '\'' +
				", phoneNumber='" + phoneNumber + '\'' +
				", beneficials=" + beneficialOwners +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Company)) return false;

		Company company = (Company) o;

		if (id != null ? !id.equals(company.id) : company.id != null) return false;
		if (name != null ? !name.equals(company.name) : company.name != null) return false;
		if (address != null ? !address.equals(company.address) : company.address != null) return false;
		if (city != null ? !city.equals(company.city) : company.city != null) return false;
		if (country != null ? !country.equals(company.country) : company.country != null) return false;
		if (email != null ? !email.equals(company.email) : company.email != null) return false;
		if (phoneNumber != null ? !phoneNumber.equals(company.phoneNumber) : company.phoneNumber != null) return false;
		return beneficialOwners != null ? beneficialOwners.equals(company.beneficialOwners) : company.beneficialOwners == null;

	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (address != null ? address.hashCode() : 0);
		result = 31 * result + (city != null ? city.hashCode() : 0);
		result = 31 * result + (country != null ? country.hashCode() : 0);
		result = 31 * result + (email != null ? email.hashCode() : 0);
		result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
		result = 31 * result + (beneficialOwners != null ? beneficialOwners.hashCode() : 0);
		return result;
	}

}
