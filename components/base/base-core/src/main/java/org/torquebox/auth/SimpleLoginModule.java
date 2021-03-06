package org.torquebox.auth;

import java.util.Map;
import java.util.Properties;

import org.jboss.security.auth.spi.UsersRolesLoginModule;

/*
 * Use of this class is deferred until AS7 integration
 */
public class SimpleLoginModule extends UsersRolesLoginModule {
    private Properties users = new Properties();
    private Properties roles = new Properties();
    
	@Override
    @SuppressWarnings("unchecked")
    protected Properties createUsers(Map<String,?> options) { 
    	Map<String,String> provided = (Map<String,String>)options.get("users");
    	this.users.putAll(provided);
        return users;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Properties createRoles(Map<String,?> options) {
    	Map<String,String> provided = (Map<String, String>) options.get("roles");
    	this.roles.putAll(provided);
        return roles;
    }

}
