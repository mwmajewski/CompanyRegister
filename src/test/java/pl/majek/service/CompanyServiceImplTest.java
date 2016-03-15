package pl.majek.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import pl.majek.model.BeneficialOwner;
import pl.majek.model.Company;
import pl.majek.model.CompanyBuilder;
import pl.majek.repository.BeneficialOwnerRepository;
import pl.majek.repository.CompanyRepository;
import pl.majek.service.exception.EntityAlreadyExistsException;
import pl.majek.service.exception.EntityNotFoundException;
import pl.majek.service.exception.InvalidEntityException;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

/**
 * Created by majewskm on 2016-03-10.
 */
@RunWith(MockitoJUnitRunner.class)
public class CompanyServiceImplTest {

	public static final long EXISTING_COMPANY_ID = 10L;
	public static final long NON_EXISTING_COMPANY_ID = 123345678L;
	private static final Long EXISTING_BENEFICIAL_OWNER_IN_COMPANY = 99L;
	CompanyService sut;

	@Mock
	CompanyRepository companyRepository;

	@Mock
	BeneficialOwnerRepository beneficialOwnerRepository;

	@Before
	public void setUp() throws Exception {
		sut = new CompanyServiceImpl();
		ReflectionTestUtils.setField(sut, "companyRepository", companyRepository);
		ReflectionTestUtils.setField(sut, "beneficialOwnerRepository", beneficialOwnerRepository);
	}

	private Company getTestCompany(){
		return new CompanyBuilder()
				.setId(EXISTING_COMPANY_ID)
				.setAddress("#1 Test Street")
				.setCity("Test City")
				.setCountry("Poland")
				.setEmail("office@company1.com")
				.setName("Company 1 Name")
				.setPhoneNumber("+45 123 123 123")
				.setBeneficialOwners(Stream.of(new BeneficialOwner("Beneficial Owner #1")
						, new BeneficialOwner("Beneficial Owner #2"))
						.collect(Collectors.toList()))
				.createCompany();
	}

	@Test
	public void shouldAddCompany() throws Exception {
		//given
		Company companyToAdd = getTestCompany();
		//when
		sut.addCompany(companyToAdd);
		//then
		verify(companyRepository).save(companyToAdd);
	}

	@Test(expected = EntityAlreadyExistsException.class)
	public void shouldThrowExceptionWhenAddingAnExistingCompany() throws Exception {
		//given
		Company companyToAdd = getTestCompany();
		when(companyRepository.exists(EXISTING_COMPANY_ID)).thenReturn(true);
		//when
		sut.addCompany(companyToAdd);
		//then
	}

	@Test
	public void shouldGetExistingCompany() throws Exception {
		//given
		when(companyRepository.exists(EXISTING_COMPANY_ID)).thenReturn(true);
		//when
		sut.getCompany(EXISTING_COMPANY_ID);
		//then
		verify(companyRepository).findOne(EXISTING_COMPANY_ID);
	}

	@Test(expected = EntityNotFoundException.class)
	public void shouldThrowExceptionWhenGettingNonExistingCompany() throws Exception {
		//given
		when(companyRepository.exists(EXISTING_COMPANY_ID)).thenReturn(false);
		//when
		sut.getCompany(EXISTING_COMPANY_ID);
		//then
	}

	@Test
	public void shouldGetAllCompanies() throws Exception {
		//given
		//when
		sut.getCompanyList();
		//then
		verify(companyRepository).findAll();
	}

	@Test(expected = EntityNotFoundException.class)
	public void shouldThrowExceptionOnUpdateNonExistingCompany() throws Exception {
		//given
		Company companyToUpdate = getTestCompany();
		when(companyRepository.exists(EXISTING_COMPANY_ID)).thenReturn(false);
		//when
		sut.updateCompany(companyToUpdate.getId(), companyToUpdate);
		//then
	}

	@Test(expected = InvalidEntityException.class)
	public void shouldThrowExceptionOnDifferentIdsWhenUpdateCompany() throws Exception {
		//given
		Company companyToUpdate = getTestCompany();
		//when
		sut.updateCompany(NON_EXISTING_COMPANY_ID, companyToUpdate);
		//then
	}

	@Test
	public void shouldGetBeneficialOwners() throws Exception {
		//given
		Company companyWithBeneficialOwners = getTestCompany();
		when(companyRepository.exists(EXISTING_COMPANY_ID)).thenReturn(true);
		when(companyRepository.findOne(EXISTING_COMPANY_ID)).thenReturn(companyWithBeneficialOwners);
		//when
		sut.getBeneficialOwners(EXISTING_COMPANY_ID);
		//then
		verify(companyRepository).findOne(EXISTING_COMPANY_ID);
	}

	@Test(expected = EntityNotFoundException.class)
	public void shouldThrowExceptionWhenGetBeneficialOwnersForNonExistingCompany() throws Exception {
		//given
		when(companyRepository.exists(EXISTING_COMPANY_ID)).thenReturn(false);
		//when
		sut.getBeneficialOwners(EXISTING_COMPANY_ID);
		//then
	}

	@Test(expected = EntityNotFoundException.class)
	public void shouldThrowExceptionWhenAddBeneficialOwnerForNonExistingCompany() throws Exception {
		//given
		when(companyRepository.exists(NON_EXISTING_COMPANY_ID)).thenReturn(false);
		BeneficialOwner beneficialOwnerToAdd = new BeneficialOwner("Beneficial Owner to Add");
		//when
		sut.addBeneficialOwner(NON_EXISTING_COMPANY_ID, beneficialOwnerToAdd);
		//then
	}

	@Test(expected = EntityAlreadyExistsException.class)
	public void shouldThrowExceptionWhenAddExistingBeneficialOwner() throws Exception {
		//given
		Company company = getTestCompany();
		company.getBeneficialOwners().get(0).setId(EXISTING_BENEFICIAL_OWNER_IN_COMPANY);
		when(companyRepository.exists(EXISTING_COMPANY_ID)).thenReturn(true);
		when(companyRepository.findOne(EXISTING_COMPANY_ID)).thenReturn(company);
		BeneficialOwner beneficialOwnerToAdd = new BeneficialOwner("Beneficial Owner to Add");
		beneficialOwnerToAdd.setId(EXISTING_BENEFICIAL_OWNER_IN_COMPANY);
		//when
		sut.addBeneficialOwner(company.getId(), beneficialOwnerToAdd);
		//then
	}

	@Test
	public void shouldAddBeneficialOwner() throws Exception {
		//given
		Company company = getTestCompany();
		when(companyRepository.exists(EXISTING_COMPANY_ID)).thenReturn(true);
		when(companyRepository.findOne(EXISTING_COMPANY_ID)).thenReturn(company);
		BeneficialOwner beneficialOwnerToAdd = new BeneficialOwner("Beneficial Owner to Add");
		//when
		sut.addBeneficialOwner(EXISTING_COMPANY_ID, beneficialOwnerToAdd);
		//then
		verify(companyRepository).findOne(EXISTING_COMPANY_ID);
		verify(beneficialOwnerRepository).save(any(BeneficialOwner.class));
		verify(companyRepository).save(company);
	}
}