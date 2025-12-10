package org.ibarra.conf;

import org.activiti.api.runtime.shared.identity.UserGroupManager;
import org.activiti.engine.impl.interceptor.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

//@Component(value = "userGroupManager")
public class CustomUserManagerFactory implements UserGroupManager {

    @Override
    public List<String> getUserGroups(String username) {
        return List.of();
    }

    @Override
    public List<String> getUserRoles(String username) {
        return List.of();
    }

    @Override
    public List<String> getGroups() {
        return List.of();
    }

    @Override
    public List<String> getUsers() {
        return List.of();
    }
}
