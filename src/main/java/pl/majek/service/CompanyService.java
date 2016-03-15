package pl.majek.service;

import pl.majek.model.BeneficialOwner;
import pl.majek.model.Company;

/**
 * Created by majewskm on 2016-03-08.
 */
public interface CompanyService {
	Company getCompany(Long companyId);

	Iterable<Company> getCompanyList();

	Company addCompany(Company company);

	Company updateCompany(Long id, Company company);

	Iterable<BeneficialOwner> getBeneficialOwners(Long id);

	BeneficialOwner addBeneficialOwner(Long id, BeneficialOwner beneficialOwner);
}
