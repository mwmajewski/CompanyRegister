package pl.majek.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.majek.model.BeneficialOwner;
import pl.majek.model.Company;
import pl.majek.repository.CompanyRepository;
import pl.majek.service.exception.EntityAlreadyExistsException;
import pl.majek.service.exception.EntityNotFoundException;
import pl.majek.service.exception.InvalidEntityException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by majewskm on 2016-02-27.
 */
@Service
@Transactional
public class CompanyServiceImpl implements CompanyService {

	@Autowired
	private CompanyRepository companyRepository;

	@Override
	public Company getCompany(Long companyId) {
		return getExistingCompany(companyId);
	}

	@Override
	public Iterable<Company> getCompanyList(){
		return companyRepository.findAll();
	}

	@Override
	public Company addCompany(Company company) {
		Long id = company.getId();
		if(checkIfCompanyIdExits(id)){
			throw new EntityAlreadyExistsException(String.format("Entity %s with companyId [%d] already exists", "Company", id));
		}
		return companyRepository.save(company);
	}

	@Override
	public Company updateCompany(Long companyId, Company company) {
		if (company != null && !companyId.equals(company.getId())) {
			throw new InvalidEntityException(String.format("Cannot save company with companyId [%d] under path where companyId = [%d]", company.getId(), companyId));
		}
		if (!checkIfCompanyIdExits(companyId)) {
			throw new EntityNotFoundException(String.format("Entity %s with companyId [%d] not found", "Company", companyId));
		}

		return companyRepository.save(company);
	}

	@Override
	public Iterable<BeneficialOwner> getBeneficialOwners(Long companyId) {
		Company company = getExistingCompany(companyId);
		List<BeneficialOwner> beneficiaries = Optional.ofNullable(company.getBeneficialOwners()).orElse(Collections.EMPTY_LIST);
		return beneficiaries;
	}

	@Override
	public void addBeneficialOwner(Long companyId, BeneficialOwner beneficialOwner) {
		Company company = getExistingCompany(companyId);
		Long beneficialOwnerId = beneficialOwner.getId();
		if(beneficialOwnerId != null && checkIfBeneficialOwnerExits(company, beneficialOwnerId)){
			throw new EntityAlreadyExistsException(String.format("Entity %s with companyId [%d] already exists", "BeneficialOwner", companyId));
		}
		List<BeneficialOwner> beneficialOwners = Optional.ofNullable(company.getBeneficialOwners()).orElse(Collections.EMPTY_LIST);
		beneficialOwners.add(beneficialOwner);
		companyRepository.save(company);
	}

	private boolean checkIfCompanyIdExits(Long companyId) {
		return companyId != null && companyRepository.exists(companyId);
	}

	private Company getExistingCompany(Long companyId) {
		if (!checkIfCompanyIdExits(companyId)) {
			throw new EntityNotFoundException(String.format("Entity %s with companyId [%d] not found", "Company", companyId));
		}
		return companyRepository.findOne(companyId);
	}

	private boolean checkIfBeneficialOwnerExits(Company company, Long beneficialOwnerId) {
		return Optional.ofNullable(company.getBeneficialOwners())
				.orElse(Collections.emptyList())
				.stream()
				.filter(beneficialOwner -> beneficialOwner.getId().equals(beneficialOwnerId))
				.findFirst()
				.isPresent();
	}
}
