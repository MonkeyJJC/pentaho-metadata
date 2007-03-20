/*
 * Copyright 2006 Pentaho Corporation.  All rights reserved. 
 * This software was developed by Pentaho Corporation and is provided under the terms 
 * of the Mozilla Public License, Version 1.1, or any later version. You may not use 
 * this file except in compliance with the license. If you need a copy of the license, 
 * please go to http://www.mozilla.org/MPL/MPL-1.1.txt. The Original Code is the Pentaho 
 * BI Platform.  The Initial Developer is Pentaho Corporation.
 *
 * Software distributed under the Mozilla Public License is distributed on an "AS IS" 
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to 
 * the license for the specific language governing your rights and limitations.
*/
package org.pentaho.pms.schema.security;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.pentaho.pms.util.Const;
import org.w3c.dom.Node;

import be.ibridge.kettle.core.XMLHandler;

public class SecurityReference
{
    private SecurityService securityService;
    
    private List /* String      */   users;
    private List /* String      */   roles;
    private List /* SecurityACL */   acls;
    
    public SecurityReference()
    {
        securityService = new SecurityService();
        
        users = new ArrayList();
        roles = new ArrayList();
        acls  = new ArrayList();
    }
    
    public String getRightsDescription(int rights)
    {
        // Go through the ACL list and if we find an ACL that matches, add it to the list
        StringBuffer desc = new StringBuffer();
        
        boolean first = true;
        boolean nothing = true;
        
        for (int i=0;i<acls.size();i++)
        {
            SecurityACL acl = (SecurityACL) acls.get(i);
            if ( (rights & acl.getMask())==acl.getMask() && acl.getMask()>0 )
            {
                if (!first) desc.append(", ");
                desc.append(acl.getName());
                
                first=false;
                nothing=false;
            }
        }
        
        if (nothing)
        {
            SecurityACL nothingAcl = findAcl(0); 
            if (nothingAcl!=null)
            {
                desc = new StringBuffer(nothingAcl.getName());
            }
        }
        
        return desc.toString();
    }
    
    private SecurityACL findAcl(int mask)
    {
        for (int i=0;i<acls.size();i++)
        {
            SecurityACL acl = (SecurityACL) acls.get(i);
            if (acl.getMask()==mask) return acl;
        }
        return null;
    }
    
    public SecurityACL findAcl(String name)
    {
        for (int i=0;i<acls.size();i++)
        {
            SecurityACL acl = (SecurityACL) acls.get(i);
            if (acl.getName().equals(name)) return acl;
        }
        return null;
    }
    
    
    public SecurityReference(SecurityService securityService) throws Exception
    {
        this();
        this.securityService = securityService;
        
        if (securityService.hasService() || securityService.hasFile())
        {
            try
            {
                Node contentNode = securityService.getContent();
                
                // Load the users
                Node usersNode = XMLHandler.getSubNode(contentNode, "users");
                int nrUsers = XMLHandler.countNodes(usersNode, "user");
                for (int i=0;i<nrUsers;i++)
                {
                    Node userNode = XMLHandler.getSubNodeByNr(usersNode, "user", i);
                    String username = XMLHandler.getNodeValue(userNode);
                    if (username!=null) users.add(username);
                }
                
                // Load the roles
                Node rolesNode = XMLHandler.getSubNode(contentNode, "roles");
                int nrRoles = XMLHandler.countNodes(rolesNode, "role");
                for (int i=0;i<nrRoles;i++)
                {
                    Node roleNode = XMLHandler.getSubNodeByNr(rolesNode, "role", i);
                    String rolename = XMLHandler.getNodeValue(roleNode);
                    if (rolename!=null) roles.add(rolename);
                }
                
                // Load the ACLs
                Node aclsNode = XMLHandler.getSubNode(contentNode, "acls");
                int nrAcls = XMLHandler.countNodes(aclsNode, "acl");
                for (int i=0;i<nrAcls;i++)
                {
                    Node aclNode = XMLHandler.getSubNodeByNr(aclsNode, "acl", i);
                    SecurityACL acl = new SecurityACL(aclNode);
                    acls.add(acl);
                }
                Collections.sort(acls); // sort by acl mask, from low to high
            }
            catch(Exception e)
            {
                throw new Exception("Unable to create new security reference object using XML string", e);
            }
        }
    }
    
    public String toXML()
    {
        StringBuffer xml = new StringBuffer();
        
        xml.append("<content>").append(Const.CR);
        
        xml.append("  <users>").append(Const.CR);
        for (int i=0;i<users.size();i++)
        {
            xml.append("    ").append(XMLHandler.addTagValue("user", (String)users.get(i)) );
        }
        xml.append("  </users>").append(Const.CR);
        
        xml.append("  <roles>").append(Const.CR);
        for (int i=0;i<roles.size();i++)
        {
            xml.append("    ").append(XMLHandler.addTagValue("role", (String)roles.get(i)) );
        }
        xml.append("  </roles>").append(Const.CR);

        xml.append("  <acls>").append(Const.CR);
        for (int i=0;i<acls.size();i++)
        {
            xml.append("    ").append(((SecurityACL)acls.get(i)).toXML() ).append(Const.CR);
        }
        xml.append("  </acls>").append(Const.CR);

        xml.append("</content>").append(Const.CR);
        
        return xml.toString();
    }

    /**
     * @return the acls
     */
    public List getAcls()
    {
        return acls;
    }

    /**
     * @param acls the acls to set
     */
    public void setAcls(List acls)
    {
        this.acls = acls;
    }

    /**
     * @return the roles
     */
    public List getRoles()
    {
        return roles;
    }

    /**
     * @param roles the roles to set
     */
    public void setRoles(List roles)
    {
        this.roles = roles;
    }

    /**
     * @return the users
     */
    public List getUsers()
    {
        return users;
    }

    /**
     * @param users the users to set
     */
    public void setUsers(List users)
    {
        this.users = users;
    }

    /**
     * @return the securityService
     */
    public SecurityService getSecurityService()
    {
        return securityService;
    }

    /**
     * @param securityService the securityService to set
     */
    public void setSecurityService(SecurityService securityService)
    {
        this.securityService = securityService;
    }

}
