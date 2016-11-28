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


            Role roleUser = new Role();
            roleUser.setName("ROLE_USER");

            Role roleAdmin = new Role();
            roleAdmin.setName("ROLE_ADMIN");

            roleUser = roleRepository.save(roleUser);
            roleAdmin = roleRepository.save(roleAdmin);

            // admin
            User admin = new User();
            admin.setName("admin");
            admin.setLogin("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRoles(new HashSet<>(Arrays.asList(roleUser, roleAdmin)));

            // user
            User user = new User();
            user.setName("user");
            user.setLogin("user");
            user.setPassword(passwordEncoder.encode("user"));
            user.setRoles(new HashSet<>(Arrays.asList(roleUser, roleAdmin)));

            // visitor
            User visitor = new User();
            visitor.setName("visitor");
            visitor.setLogin("visitor");
            visitor.setPassword(passwordEncoder.encode("visitor"));
            visitor.setRoles(new HashSet<>(Arrays.asList(roleUser, roleAdmin)));


            userRepository.save(Arrays.asList(admin, user, visitor));

            // client

            Client client = new Client("clientapp", passwordEncoder.encode("clientapp"), "read,write");
            clientRepository.save(client);

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
