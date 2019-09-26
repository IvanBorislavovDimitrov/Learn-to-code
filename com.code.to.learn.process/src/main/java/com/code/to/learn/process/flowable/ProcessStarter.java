package com.code.to.learn.process.flowable;

import com.code.to.learn.api.model.UserBindingModel;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.stereotype.Component;

@Component
public class ProcessStarter {

    public void startUserRegisterProcess(UserBindingModel userBindingModel) {
        ProcessEngineConfiguration processEngineConfiguration = createDefaultProcessEngineConfiguration();
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("diagram/register-user.bpmn20.xml")
                .deploy();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId())
                .singleResult();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        runtimeService.startProcessInstanceByKey(processDefinition.getKey());
    }

    private ProcessEngineConfiguration createDefaultProcessEngineConfiguration() {
        return new StandaloneProcessEngineConfiguration()
                .setJdbcUrl("jdbc:postgresql://localhost:5432/code")
                .setJdbcUsername("postgres")
                .setJdbcPassword("34273427")
                .setJdbcDriver("org.postgresql.Driver")
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
    }
}
