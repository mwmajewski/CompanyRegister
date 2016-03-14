package pl.majek.model;

import java.util.List;

public class CompanyBuilder {
	private Long id;
	private String name;
	private String address;
	private String city;
	private String country;
	private String email;
	private String phoneNumber;
	private List<BeneficialOwner> beneficialOwners;

	public CompanyBuilder setId(Long id) {
		this.id = id;
		return this;
	}

	public CompanyBuilder setName(String name) {
		this.name = name;
		return this;
	}

	public CompanyBuilder setAddress(String address) {
		this.address = address;
		return this;
	}

	public CompanyBuilder setCity(String city) {
		this.city = city;
		return this;
	}

	public CompanyBuilder setCountry(String country) {
		this.country = country;
		return this;
	}

	public CompanyBuilder setEmail(String email) {
		this.email = email;
		return this;
	}

	public CompanyBuilder setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
		return this;
	}

	public CompanyBuilder setBeneficialOwners(List<BeneficialOwner> beneficialOwners) {
		this.beneficialOwners = beneficialOwners;
		return this;
	}

	public Company createCompany() {
		return new Company(id, name, address, city, country, email, phoneNumber, beneficialOwners);
	}
}