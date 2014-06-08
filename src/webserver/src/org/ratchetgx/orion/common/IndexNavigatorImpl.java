package org.ratchetgx.orion.common;

import java.util.Collection;
import java.util.Iterator;

import org.ratchetgx.orion.module.common.service.IndexNavigator;
import org.ratchetgx.orion.module.common.service.IndexService;
import org.ratchetgx.orion.security.SsfwGrantedAuthority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


@Component("indexNavigator")
public class IndexNavigatorImpl implements IndexNavigator  {
	private Logger log = LoggerFactory.getLogger(this.getClass());

	
	public String navigate(UserDetails userDetails){
		Collection list = (Collection) userDetails.getAuthorities();
		Iterator iter = list.iterator();
		while (iter.hasNext()) {
			SsfwGrantedAuthority ga = (SsfwGrantedAuthority) iter.next();
			String role = ga.getAuthority();
			if("本科生".equals(role)){				
				log.debug("跳转菜单");
				return "/zzfw/framework/login/index.do";
			}
		}
		return null;
	}
}
