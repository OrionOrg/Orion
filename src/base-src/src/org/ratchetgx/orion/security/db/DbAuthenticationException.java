/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ratchetgx.orion.security.db;

import org.springframework.security.core.AuthenticationException;

/**
 *
 * @author hrfan
 */
public class DbAuthenticationException extends AuthenticationException {

    public DbAuthenticationException(String msg) {
        super(msg);
    }
}
