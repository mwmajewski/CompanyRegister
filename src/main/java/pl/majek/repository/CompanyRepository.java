package pl.majek.repository;

import org.springframework.data.repository.CrudRepository;
import pl.majek.model.Company;

import java.util.List;

/**
 * Created by majewskm on 2016-02-27.
 */
public interface CompanyRepository extends CrudRepository<Company, Long> {}
