package com.quazzom.active_directory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class ActiveDirectoryUtilsTest {
	@Test
	void testGetActiveDirectoryErrorMessage() throws ActiveDirectoryException {
		// test a null error message
		Throwable ex1 = assertThrows(ActiveDirectoryException.class, () -> {
			ActiveDirectoryUtils.getActiveDirectoryErrorMessage(null);
		});
		assertEquals("The error message isn't a LDAP error message: null.", ex1.getMessage());

		// test a empty error message
		Throwable ex2 = assertThrows(ActiveDirectoryException.class, () -> {
			ActiveDirectoryUtils.getActiveDirectoryErrorMessage("");
		});
		assertEquals("The error message isn't a LDAP error message: .", ex2.getMessage());

		// a simple, but not valid LDAP error message
		Throwable ex3 = assertThrows(ActiveDirectoryException.class, () -> {
			ActiveDirectoryUtils.getActiveDirectoryErrorMessage("LDAP: error code");
		});
		assertEquals("The error message isn't a LDAP error message: LDAP: error code.", ex3.getMessage());

		// test a valid LDAP error message. LDAP error 49 in PT-BR :)
		String validErrorMessage = ActiveDirectoryUtils.getActiveDirectoryErrorMessage(
				"LDAP: error code 49 - 80090308: LdapErr: DSID-0C090439, comment: AcceptSecurityContext error, data 52e, v4563");
		assertEquals("LDAP error 49: Usuário e/ou senha inválido(s).", validErrorMessage);
	}
}
