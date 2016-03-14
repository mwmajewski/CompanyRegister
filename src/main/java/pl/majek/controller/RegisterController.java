package pl.majek.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.majek.model.BeneficialOwner;
import pl.majek.model.Company;
import pl.majek.service.CompanyService;

import javax.validation.Valid;

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
	public Company addCompany(@RequestBody @Valid Company company) {
		return companyService.addCompany(company);
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	public Company updateCompany(@PathVariable long id, @RequestBody @Valid Company company) {
		return companyService.updateCompany(id, company);
	}

	@RequestMapping(path = "/{id}/beneficialOwners", method = RequestMethod.GET)
	public Iterable<BeneficialOwner> getBeneficialOwners(@PathVariable long id) {
		return companyService.getBeneficialOwners(id);
	}

	@RequestMapping(path = "/{id}/beneficialOwners", method = RequestMethod.POST)
	public void addBeneficialOwner(@PathVariable long id, @RequestBody @Valid BeneficialOwner beneficialOwner) {
		companyService.addBeneficialOwner(id, beneficialOwner);
	}

}
