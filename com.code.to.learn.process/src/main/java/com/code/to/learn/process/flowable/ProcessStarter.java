package com.code.to.learn.process.flowable;

import com.code.to.learn.api.model.UserBindingModel;
import com.code.to.learn.persistence.constant.DatabaseProperties;
import com.code.to.learn.persistence.hibernate.HibernateUtils;
import com.code.to.learn.process.constant.Constants;
import com.code.to.learn.process.parser.ParserFactory;
import com.code.to.learn.process.parser.ParserType;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ProcessStarter {

    public void startUserRegisterProcess(UserBindingModel userBindingModel) {
        ProcessEngineConfiguration processEngineConfiguration = createDefaultProcessEngineConfiguration();
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource(Constants.REGISTER_USER_PROCESS)
                .deploy();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId())
                .singleResult();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        runtimeService.startProcessInstanceByKey(processDefinition.getKey(), createUserProcessVariables(userBindingModel));
    }

    private ProcessEngineConfiguration createDefaultProcessEngineConfiguration() {
        Map<String, Object> databaseProperties = getDatabaseProperties();
        return new StandaloneProcessEngineConfiguration()
                .setJdbcUrl((String) databaseProperties.get(Constants.DATABASE_URL_KEY))
                .setJdbcUsername(DatabaseProperties.DATABASE_USERNAME)
                .setJdbcPassword(DatabaseProperties.DATABASE_PASSWORD)
                .setJdbcDriver((String) databaseProperties.get(Constants.DATABASE_DRIVER_KEY))
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
    }

    private Map<String, Object> getDatabaseProperties() {
        try (SessionFactory sessionFactory = HibernateUtils.getSessionFactory()) {
            Map<String, Object> properties = sessionFactory.getProperties();
            sessionFactory.close();
            return properties;
        }
    }

    private Map<String, Object> createUserProcessVariables(UserBindingModel userBindingModel) {
        String userBindingModelString = ParserFactory.createParser(ParserType.JSON).serialize(userBindingModel);
        Map<String, Object> userProcessVariables = new HashMap<>();
        userProcessVariables.put(Constants.USER_BINDING_MODEL_KEY, userBindingModelString);
        return userProcessVariables;
    }
}
