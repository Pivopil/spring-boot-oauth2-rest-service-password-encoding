package io.github.pivopil;

import io.github.pivopil.share.entities.impl.Client;
import io.github.pivopil.share.entities.impl.Role;
import io.github.pivopil.share.entities.impl.User;
import io.github.pivopil.share.persistence.ClientRepository;
import io.github.pivopil.share.persistence.RoleRepository;
import io.github.pivopil.share.persistence.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Created on 17.06.16.
 */

@SpringBootApplication
@ComponentScan("io.github.pivopil")
public class Application extends SpringBootServletInitializer implements CommandLineRunner {

    @Autowired
    private ApplicationContext applicationContext;

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String... args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    @Override
    public void run(String... args) throws Exception {
        PasswordEncoder passwordEncoder = applicationContext.getBean(PasswordEncoder.class);
        UserRepository userRepository = applicationContext.getBean(UserRepository.class);
        if (((List<User>) userRepository.findAll()).size() == 0) {
            RoleRepository roleRepository = applicationContext.getBean(RoleRepository.class);
            ClientRepository clientRepository  = applicationContext.getBean(ClientRepository.class);

            Role roleAdmin = new Role();
            roleAdmin.setName("ROLE_ADMIN");
            roleAdmin = roleRepository.save(roleAdmin);

            Role roleUser = new Role();
            roleUser.setName("ROLE_USER");
            roleUser = roleRepository.save(roleUser);

            Role roleClient = new Role();
            roleClient.setName("ROLE_CLIENT");
            roleClient = roleRepository.save(roleClient);


            Role roleClientFirst = new Role();
            roleClientFirst.setName("ROLE_FIRST");
            roleClientFirst = roleRepository.save(roleClientFirst);

            Role roleClientSecond = new Role();
            roleClientSecond.setName("ROLE_SECOND");
            roleClientSecond = roleRepository.save(roleClientSecond);


            // admin with roles for all
            User admin = new User();
            admin.setName("adminName");
            admin.setLogin("adminLogin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRoles(new HashSet<>(Arrays.asList(roleUser, roleAdmin, roleClientFirst, roleClientSecond, roleClient)));

            // admin for first client
            User clientAdminFirst = new User();
            clientAdminFirst.setName("clientFirstName");
            clientAdminFirst.setLogin("clientFirstLogin");
            clientAdminFirst.setPassword(passwordEncoder.encode("clientFirst"));
            clientAdminFirst.setRoles(new HashSet<>(Arrays.asList(roleClientFirst, roleClient)));

            // admin for second client
            User clientAdminSecond = new User();
            clientAdminSecond.setName("clientSecondName");
            clientAdminSecond.setLogin("clientSecondLogin");
            clientAdminSecond.setPassword(passwordEncoder.encode("clientSecond"));
            clientAdminSecond.setRoles(new HashSet<>(Arrays.asList(roleClientSecond, roleClient)));

            // user for first client
            User userFirst = new User();
            userFirst.setName("userFirstName");
            userFirst.setLogin("userFirstLogin");
            userFirst.setPassword(passwordEncoder.encode("userFirst"));
            userFirst.setRoles(new HashSet<>(Arrays.asList(roleUser, roleClientFirst)));

            // user for client second
            User userSecond = new User();
            userSecond.setName("userSecondName");
            userSecond.setLogin("userSecondLogin");
            userSecond.setPassword(passwordEncoder.encode("userSecond"));
            userSecond.setRoles(new HashSet<>(Arrays.asList(roleUser, roleClientSecond)));

            userRepository.save(Arrays.asList(admin, userFirst, userSecond, clientAdminFirst, clientAdminSecond));

            // clients
            Client client1 = new Client("clientFirst", passwordEncoder.encode("clientFirst"), "read,write");
            Client client2 = new Client("clientSecond", passwordEncoder.encode("clientSecond"), "read,write");
            clientRepository.save(Arrays.asList(client1, client2));

            // acl and token tables creation

//            try {
//                DataSource dataSource = applicationContext.getBean(DataSource.class);
//                Connection connection = dataSource.getConnection();
//                ScriptUtils.executeSqlScript(connection, new EncodedResource(new ClassPathResource("/sql/acl-token-tables.sql"), "utf8"), false, false, "--", "commit", "______", "__________=");
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }

        }
    }

}
