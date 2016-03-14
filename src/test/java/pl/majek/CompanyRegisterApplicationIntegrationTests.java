package pl.majek;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.majek.controller.RegisterController;
import pl.majek.model.BeneficialOwner;
import pl.majek.model.Company;
import pl.majek.model.CompanyBuilder;
import pl.majek.repository.CompanyRepository;
import pl.majek.service.CompanyService;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {CompanyRegisterApplication.class})
@WebAppConfiguration
public class CompanyRegisterApplicationIntegrationTests {

	@Autowired
	RegisterController registerController;

	@Autowired
	CompanyService companyService;

	@Autowired
	CompanyRepository companyRepository;

	private MockMvc mockMvc;

	private ObjectMapper om = new ObjectMapper();

	@Before
	public void setUp(){
		mockMvc = MockMvcBuilders.standaloneSetup(registerController).build();
	}

	private Company getTestCompany(){
		return new CompanyBuilder()
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
		byte[] companyToAddJson = om.writerFor(Company.class).writeValueAsBytes(companyToAdd);
		//when
		MvcResult mvcResult = mockMvc.perform(
				MockMvcRequestBuilders.post("/companies")
						.content(companyToAddJson)
						.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		//then
		Company responseCompany = om.readValue(mvcResult.getResponse().getContentAsString(), Company.class);
		Company savedCompany = companyRepository.findOne(responseCompany.getId());
		Assert.assertNotNull(savedCompany);
		Assert.assertEquals(companyToAdd.getName(), savedCompany.getName());
		Assert.assertEquals(companyToAdd.getAddress(), savedCompany.getAddress());
		Assert.assertEquals(companyToAdd.getCity(), savedCompany.getCity());
		Assert.assertEquals(companyToAdd.getCountry(), savedCompany.getCountry());
		Assert.assertEquals(companyToAdd.getEmail(), savedCompany.getEmail());
		Assert.assertEquals(companyToAdd.getPhoneNumber(), savedCompany.getPhoneNumber());
	}

	@Test
	public void shouldReturn409WhenTryingToAddCompanyWithExistingId() throws Exception {
		//given
		Company companyToAdd = companyRepository.save(getTestCompany());
		byte[] companyToAddJson = om.writerFor(Company.class).writeValueAsBytes(companyToAdd);
		//when
		mockMvc.perform(
				MockMvcRequestBuilders.post("/companies")
						.content(companyToAddJson)
						.contentType(MediaType.APPLICATION_JSON_UTF8))
		//then
						.andExpect(MockMvcResultMatchers.status().isConflict());
	}

	@Test
	public void shouldUpdateCompany() throws Exception {
		//given
		Company companyToUpdate = companyRepository.save(getTestCompany());
		String newCompanyName = "Changed Company Name";
		companyToUpdate.setName(newCompanyName);
		byte[] companyToAddJson = om.writerFor(Company.class).writeValueAsBytes(companyToUpdate);
		//when
		MvcResult mvcResult = mockMvc.perform(
				MockMvcRequestBuilders.put("/companies/{id}", companyToUpdate.getId())
						.content(companyToAddJson)
						.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		//then
		Company responseCompany = om.readValue(mvcResult.getResponse().getContentAsString(), Company.class);
		Company updatedCompany = companyRepository.findOne(responseCompany.getId());
		Assert.assertNotNull(updatedCompany);
		Assert.assertEquals(newCompanyName, updatedCompany.getName());
		Assert.assertEquals(companyToUpdate.getAddress(), updatedCompany.getAddress());
	}

	@Test
	public void shouldReturn404WhenTryingToUpdateNonExistentCompanyById() throws Exception {
		//given
		long nonExistentId = 1234134234L;
		Company companyToUpdate = getTestCompany();
		companyToUpdate.setId(nonExistentId);
		byte[] companyToAddJson = om.writerFor(Company.class).writeValueAsBytes(companyToUpdate);
		//when
		mockMvc.perform(MockMvcRequestBuilders.put("/companies/{id}", nonExistentId)
				.content(companyToAddJson)
				.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.status()
				.isNotFound());
	}

	@Test
	public void shouldReturn409WhenTryingToUpdateCompanyUsingWrongIdInPath() throws Exception {
		//given
		long nonExistentId = 1234134234L;
		Company companyToUpdate = companyRepository.save(getTestCompany());
		byte[] companyToAddJson = om.writerFor(Company.class).writeValueAsBytes(companyToUpdate);
		//when
		mockMvc.perform(MockMvcRequestBuilders.put("/companies/{id}", nonExistentId)
				.content(companyToAddJson)
				.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.status()
				.isConflict());
	}

	@Test
	public void shouldGetCompanyById() throws Exception {
		//given
		Company companyToGet = companyRepository.save(getTestCompany());
		//when
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/companies/{id}", companyToGet.getId())
										.accept(MediaType.APPLICATION_JSON_UTF8))
										.andExpect(MockMvcResultMatchers.status().isOk())
										.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
										.andReturn();
		//then
		Company responseCompany = om.readValue(mvcResult.getResponse().getContentAsString(), Company.class);
		Assert.assertEquals(companyToGet.getId(), responseCompany.getId());
	}

	@Test
	public void shouldReturn404WhenTryingToGetNonExistentCompanyById() throws Exception {
		//given
		long nonExistentId = 1234134234L;
		//when
		mockMvc.perform(MockMvcRequestBuilders.get("/companies/{id}", nonExistentId)
				.accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	@Test
	public void shouldGetAllCompanies() throws Exception {
		//given
		Company companyToAdd1 = companyRepository.save(getTestCompany());
		Company companyToAdd2 = companyRepository.save(getTestCompany());
		Company companyToAdd3 = companyRepository.save(getTestCompany());
		//when
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/companies").accept(MediaType.APPLICATION_JSON_UTF8))
										.andExpect(MockMvcResultMatchers.status().isOk())
										.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
										.andReturn();
		//then
		List<Company> responseCompanies = om.readValue(mvcResult.getResponse().getContentAsString()
													, new TypeReference<List<Company>>(){});
		Assert.assertTrue(responseCompanies.stream().filter(company -> company.getId().equals(companyToAdd1.getId())).count() == 1);
		Assert.assertTrue(responseCompanies.stream().filter(company -> company.getId().equals(companyToAdd2.getId())).count() == 1);
		Assert.assertTrue(responseCompanies.stream().filter(company -> company.getId().equals(companyToAdd3.getId())).count() == 1);
	}

	@Test
	public void shouldGetBeneficialOwnersForACompany() throws Exception {
		//given
		Company companyWithBeneficialOwners = companyRepository.save(getTestCompany());
		//when
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/companies/{id}/beneficialOwners", companyWithBeneficialOwners.getId())
									.accept(MediaType.APPLICATION_JSON_UTF8))
									.andExpect(MockMvcResultMatchers.status().isOk())
									.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).andReturn();

		//then
		List<BeneficialOwner> responseBeneficialOwners = om.readValue(mvcResult.getResponse().getContentAsString()
				, new TypeReference<List<BeneficialOwner>>(){});
		Assert.assertTrue(responseBeneficialOwners.stream().filter(bo -> bo.getId().equals(companyWithBeneficialOwners.getBeneficialOwners().get(0).getId())).count() == 1);
		Assert.assertTrue(responseBeneficialOwners.stream().filter(bo -> bo.getId().equals(companyWithBeneficialOwners.getBeneficialOwners().get(1).getId())).count() == 1);
	}

	@Test
	public void shouldReturn404WhenTryingToGetBeneficialOwnersForNonExistentCompany() throws Exception {
		//given
		long nonExistentId = 1234134234L;
		//when
		mockMvc.perform(MockMvcRequestBuilders.get("/companies/{id}/beneficialOwners", nonExistentId)
				.accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	@Test
	public void shouldAddABeneficialOwner() throws Exception {
		//given
		Company companyWithBeneficialOwners = companyRepository.save(getTestCompany());
		final String beneficialOwnerName = "BeneficialOwnerTestUniqueName";
		BeneficialOwner bo = new BeneficialOwner();
		bo.setName(beneficialOwnerName);
		byte[] boJson = om.writerFor(BeneficialOwner.class).writeValueAsBytes(bo);
		//when
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/companies/{id}/beneficialOwners", companyWithBeneficialOwners.getId())
									.content(boJson)
									.contentType(MediaType.APPLICATION_JSON_UTF8))
									.andExpect(MockMvcResultMatchers.status().isOk())
									.andReturn();
		//then
		Company savedCompany = companyRepository.findOne(companyWithBeneficialOwners.getId());
		Assert.assertTrue("Beneficial Owners List is not empty",!savedCompany.getBeneficialOwners().isEmpty());
		Assert.assertTrue(String.format("There should be one beneficiary with this name: [%s]", beneficialOwnerName) ,
						1 == savedCompany.getBeneficialOwners()
								.stream()
								.filter(beneficialOwner -> beneficialOwnerName.equals(beneficialOwner.getName()))
								.count());

	}

	@Test
	public void shouldReturn409WhenTryingToAddBeneficialOwnerWithAlreadyExistingId() throws Exception {
		//given
		Company companyWithBeneficialOwners = companyRepository.save(getTestCompany());
		BeneficialOwner alreadyExistingBeneficialOwner = companyWithBeneficialOwners.getBeneficialOwners().get(0);
		byte[] boJson = om.writerFor(BeneficialOwner.class).writeValueAsBytes(alreadyExistingBeneficialOwner);
		//when
		mockMvc.perform(MockMvcRequestBuilders.post("/companies/{id}/beneficialOwners", companyWithBeneficialOwners.getId())
			.content(boJson)
			.contentType(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(MockMvcResultMatchers.status().isConflict());
		//then
	}
}
