package io.github.pivopil.rest.services;

import io.github.pivopil.share.entities.impl.Company;
import io.github.pivopil.share.entities.impl.Role;
import io.github.pivopil.share.persistence.CompanyRepository;
import io.github.pivopil.share.persistence.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

/**
 * Created on 08.01.17.
 */

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    private final RoleRepository roleRepository;

    @Autowired
    public CompanyService(CompanyRepository companyRepository, RoleRepository roleRepository) {
        this.companyRepository = companyRepository;
        this.roleRepository = roleRepository;
    }

    public Iterable<Company> list() {
        return companyRepository.findAll();
    }

    public Company getSingle(Long id) {
        return companyRepository.findOne(id);
    }

    // todo add validation
    @Transactional
    public Company save(Company company) {
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

    // todo add validation
    @Transactional
    public void update(Company company) {
        Company companyFromRepository = companyRepository.findOne(company.getId());

        if (!companyFromRepository.getRoleAlias().equals(company.getRoleAlias())) {
            throw new IllegalArgumentException("You can not change Role Alias of the company");
        }

        companyRepository.save(company);
    }

    @Transactional
    public void delete(Long id) {
        companyRepository.delete(id);
    }
}
