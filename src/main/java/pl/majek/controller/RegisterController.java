package pl.majek.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import pl.majek.model.BeneficialOwner;
import pl.majek.model.Company;
import pl.majek.service.CompanyService;

import javax.validation.Valid;
import java.net.URI;

/**
 * Created by majewskm on 2016-02-27.
 */
@RestController
@RequestMapping(path = "/companies")
public class RegisterController {

	@Autowired
	private CompanyService companyService;

	@RequestMapping(path = "/{id}", method = RequestMethod.GET )
	public Company getCompany(@PathVariable long id) {
		return companyService.getCompany(id);
	}

	@RequestMapping(path = "", method = RequestMethod.GET)
	public Iterable<Company> getCompanyList() {
		Iterable<Company> list = companyService.getCompanyList();
		return list;
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public ResponseEntity<Void> addCompany(@RequestBody @Valid Company company) {
		Company addedCompany = companyService.addCompany(company);
		URI location = UriComponentsBuilder.fromPath("/companies/{id}").buildAndExpand(addedCompany.getId()).toUri();
		return ResponseEntity.created(location).build();
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	public void updateCompany(@PathVariable long id, @RequestBody @Valid Company company) {
		companyService.updateCompany(id, company);
	}

	@RequestMapping(path = "/{id}/beneficialOwners", method = RequestMethod.GET)
	public Iterable<BeneficialOwner> getBeneficialOwners(@PathVariable long id) {
		return companyService.getBeneficialOwners(id);
	}

	@RequestMapping(path = "/{id}/beneficialOwners", method = RequestMethod.POST)
	public ResponseEntity<Void> addBeneficialOwner(@PathVariable long id, @RequestBody @Valid BeneficialOwner beneficialOwner) {
		BeneficialOwner addedBeneficialOwner = companyService.addBeneficialOwner(id, beneficialOwner);
		URI location = UriComponentsBuilder
				.fromPath("/companies/{id}/beneficialOwners/{addedBeneficialOwner.id}")
				.buildAndExpand(id, addedBeneficialOwner.getId())
				.toUri();
		return ResponseEntity.created(location).build();
	}

}
