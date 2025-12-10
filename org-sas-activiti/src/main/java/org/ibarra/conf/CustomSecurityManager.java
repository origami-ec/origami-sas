package org.ibarra.conf;

import org.activiti.api.runtime.shared.security.SecurityManager;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class CustomSecurityManager implements SecurityManager {
    @Override
    public String getAuthenticatedUserId() {
        return "";
    }

    @Override
    public List<String> getAuthenticatedUserGroups() throws SecurityException {
        return List.of();
    }

    @Override
    public List<String> getAuthenticatedUserRoles() throws SecurityException {
        return List.of();
    }
}
