package org.sas.zull.service;

import org.sas.zull.dto.ActiveDirectoryModel;
import org.sas.zull.conf.AppProps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.*;
import javax.naming.ldap.Control;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import javax.naming.ldap.PagedResultsControl;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


@Service
public class ActiveDirectory {

    @Autowired
    private AppProps appProps;
    private static Logger LOG = LoggerFactory.getLogger(ActiveDirectory.class);
    /*
    app:
      activeDirectory:
        host: 172.20.1.2
        port: 389
        auth: simple
        referral: follow
        context: com.sun.jndi.ldap.LdapCtxFactory
     */

    public ActiveDirectoryModel getConnection(String username, String password) throws Exception {
        String ldap = "ldap://" + appProps.getAdHost() + ":" + appProps.getAdPort();
        ActiveDirectoryModel directoryModel = new ActiveDirectoryModel();
        Properties props = new Properties();
        props.put(Context.INITIAL_CONTEXT_FACTORY, appProps.getAdContext());

        props.put(Context.PROVIDER_URL, ldap);

        props.put(Context.SECURITY_PRINCIPAL, appProps.getAdUsuario());
        props.put(Context.SECURITY_CREDENTIALS, appProps.getAdClave());//dn user password


        InitialDirContext context = new InitialDirContext(props);

        SearchControls ctrls = new SearchControls();

        ctrls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        NamingEnumeration<SearchResult> answers = context.search(appProps.getAdDn(), "(|(sAMAccountName=" + username + ")(cn=" + username + ")(userPrincipalName=" + username +"))", ctrls);

        javax.naming.directory.SearchResult result = answers.nextElement();

        String user = result.getNameInNamespace();

        try {
            props = new Properties();
            props.put(Context.INITIAL_CONTEXT_FACTORY, appProps.getAdContext());
            props.put(Context.PROVIDER_URL, ldap);
            props.put(Context.SECURITY_PRINCIPAL, user);
            props.put(Context.SECURITY_CREDENTIALS, password);

            DirContext ctx = new InitialDirContext(props);
            Attributes atts = ctx.getAttributes("");
            //System.out.println("atts: " + atts.toString());
            ctx.close();
            System.out.println("\n*** Authenticated ~" + user + "~ ***\n");
            directoryModel.setEstado(Boolean.TRUE);
            directoryModel.setResponse(user);
            return directoryModel;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("\n*** Not Authenticated ***\n");
            directoryModel.setEstado(Boolean.FALSE);
            return directoryModel;
        }
    }

    public ActiveDirectoryModel authenticateJndi2(String username, String password) throws Exception {
        ActiveDirectoryModel directoryModel = new ActiveDirectoryModel();
        Properties props = new Properties();
        props.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");

        props.put(Context.PROVIDER_URL, "ldap://" + appProps.getAdHost() + ":" + appProps.getAdPort());

        props.put(Context.SECURITY_PRINCIPAL, appProps.getAdUsuario());
        props.put(Context.SECURITY_CREDENTIALS, appProps.getAdClave());//dn user password


        String wantedAttribute = "givenName";
        //String[] attributeFilter = {"givenName", "sAMAccountName", "name", "userPrincipalName", "sn", "memberOf"};
        //String[] attributeFilter = {wantedAttribute};

        int pageSize = 999;
        LdapContext context = new InitialLdapContext(props, new Control[]{
                new PagedResultsControl(pageSize, Control.CRITICAL)});

        SearchControls ctrl = new SearchControls();
//        InitialDirContext context = new InitialDirContext(props);
        //ctrl.setReturningAttributes(attributeFilter);
        //ctrl.setReturningAttributes(new String[]{"givenName", "sn", "memberOf"});
        ctrl.setCountLimit(3000);

        ctrl.setSearchScope(SearchControls.SUBTREE_SCOPE);
        String dn = "DC=local,DC=ibarra,DC=gob,DC=ec";
        //NamingEnumeration<SearchResult> enumeration = context.search(dn, "(objectClass=person)", ctrl);
        NamingEnumeration<SearchResult> enumeration = context.search(dn, "(& (objectClass=person) (userPrincipalName=*local.ibarra.gob.ec*))", ctrl);

        List<String> usuariosAD = new ArrayList<>();
        int i = 1;
        while (enumeration.hasMore()) {
            SearchResult result = enumeration.next();
            Attributes attribs = result.getAttributes();
            LOG.info(i + ": " + attribs.toString());
            //System.out.println(i + ": " + attribs.toString());
            usuariosAD.add(attribs.toString());
            i++;
        }

        try {
            System.out.println("\n*** Authenticated ~~ ***\n");
            directoryModel.setEstado(Boolean.TRUE);
            directoryModel.setResponse(usuariosAD.toString());
            return directoryModel;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("\n*** Not Authenticated ***\n");
            directoryModel.setEstado(Boolean.FALSE);
            return directoryModel;
        }
    }

}