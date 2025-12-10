package org.ibarra.conf;

import org.activiti.api.process.model.payloads.GetProcessDefinitionsPayload;
import org.activiti.api.process.model.payloads.GetProcessInstancesPayload;
import org.activiti.core.common.spring.security.policies.ProcessSecurityPoliciesManager;
import org.activiti.core.common.spring.security.policies.SecurityPolicyAccess;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Set;

@Configuration
public class CustomPoliciesManager implements ProcessSecurityPoliciesManager {
    @Override
    public GetProcessDefinitionsPayload restrictProcessDefQuery(SecurityPolicyAccess securityPolicyAccess) {
        return new GetProcessDefinitionsPayload();
    }

    @Override
    public GetProcessInstancesPayload restrictProcessInstQuery(SecurityPolicyAccess securityPolicyAccess) {
        return new GetProcessInstancesPayload();
    }

    @Override
    public boolean canRead(String processDefinitionKey, String serviceName) {
        return true;
    }

    @Override
    public boolean canWrite(String processDefinitionKey, String serviceName) {
        return true;
    }

    @Override
    public boolean canRead(String processDefinitionKey) {
        return true;
    }

    @Override
    public boolean canWrite(String processDefinitionKey) {
        return true;
    }

    @Override
    public boolean arePoliciesDefined() {
        return true;
    }

    @Override
    public Map<String, Set<String>> getAllowedKeys(SecurityPolicyAccess... securityPolicyAccess) {
        return Map.of();
    }
}
