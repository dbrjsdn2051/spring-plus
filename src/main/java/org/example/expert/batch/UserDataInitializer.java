package org.example.expert.batch;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class UserDataInitializer {

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddlAuto;

    private final BatchInsertUserService batchInsertUserService;

    @Bean
    public static BeanDefinitionRegistryPostProcessor jobRegistryBeanPostProcessorRemover() {
        return registry -> registry.removeBeanDefinition("jobRegistryBeanPostProcessor");
    }

    @PostConstruct
    public void executeBatchInsert() {
        if (!ddlAuto.equals("create")) return;
        batchInsertUserService.insertUser(1_000_000);
    }
}
