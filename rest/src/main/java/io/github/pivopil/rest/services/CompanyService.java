package io.github.pivopil.rest.services;

import io.github.pivopil.share.builders.Builders;
import io.github.pivopil.share.builders.impl.CompanyBuilder;
import io.github.pivopil.share.entities.impl.Company;
import io.github.pivopil.share.entities.impl.Role;
import io.github.pivopil.share.persistence.CompanyRepository;
import io.github.pivopil.share.persistence.RoleRepository;
import net.sf.oval.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

/**
 * Created on 08.01.17.
 */

@Service
@DependsOn("ovalValidator")
public class CompanyService {

    private final CompanyRepository companyRepository;

    private final RoleRepository roleRepository;

    private final Validator ovalValidator;

    @Autowired
    public CompanyService(CompanyRepository companyRepository, RoleRepository roleRepository, Validator ovalValidator) {
        this.companyRepository = companyRepository;
        this.roleRepository = roleRepository;
        this.ovalValidator = ovalValidator;
    }

    public Iterable<Company> list() {
        return companyRepository.findAll();
    }

    public Company getSingle(Long id) {
        return companyRepository.findOne(id);
    }

    @Transactional
    public Company save(Company company) {

        CompanyBuilder companyBuilder = Builders.of(company);

        company = companyBuilder.withOvalValidator(ovalValidator).build();

        company = companyRepository.save(company);

        String upperCase = company.getRoleAlias().toUpperCase();

        Role roleUser = new Role();
        String roleUserName = String.format("ROLE_%s_LOCAL_USER", upperCase);
        roleUser.setName(roleUserName);

        Role roleAdmin = new Role();
        String roleAdminName = String.format("ROLE_%s_LOCAL_ADMIN", upperCase);
        roleAdmin.setName(roleAdminName);

        roleRepository.save(Arrays.asList(roleAdmin, roleUser));

        return company;
    }

    @Transactional
    public void update(Company company) {
        Company companyFromRepository = companyRepository.findOne(company.getId());

        if (!companyFromRepository.getRoleAlias().equals(company.getRoleAlias())) {
            throw new IllegalArgumentException("You can not change Role Alias of the company");
        }

        CompanyBuilder companyBuilder = Builders.of(company);

        company = companyBuilder.withOvalValidator(ovalValidator).build();

        companyRepository.save(company);
    }

    @Transactional
    public void delete(Long id) {
        companyRepository.delete(id);
    }
}
