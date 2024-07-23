package com.quazzom.active_directory;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.when;

import java.util.List;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class ActiveDirectoryTest {
	@Mock
	Attribute attribute;

	@Mock
	Attributes attributes;

	@Mock
	SearchResult searchResult;

	@Mock
	NamingEnumeration<SearchResult> searchResultEnumeration;

	@Mock
	DirContext dirContext;

	@InjectMocks
	ActiveDirectory ad = new ActiveDirectory("a", "b", "0000", "LAB01", "ACME");

	@Test
	void testSearchUserGroups() throws NamingException, ActiveDirectoryException {

		ReflectionTestUtils.setField(ad, "isHandShakeMade", true);

		// teaching the attribute
		when(attribute.size()).thenReturn(7);
		when(attribute.get(0)).thenReturn("CN=firewall-advance,OU=Firewall Groups,DC=LAB01,DC=ACME");
		when(attribute.get(1)).thenReturn("CN=firewall-vpn,OU=Firewall Groups,DC=LAB01,DC=ACME");
		when(attribute.get(2)).thenReturn("CN=firewall-inf,OU=Firewall Groups,DC=LAB01,DC=ACME");
		when(attribute.get(3)).thenReturn("CN=inf,OU=INF,OU=room,DC=LAB01,DC=ACME");
		when(attribute.get(4)).thenReturn("CN=Color Print,OU=room,DC=LAB01,DC=ACME");
		when(attribute.get(5)).thenReturn("CN=Administrators,CN=Builtin,DC=LAB01,DC=ACME");
		when(attribute.get(6)).thenReturn("CN=Domain Admins,CN=Users,DC=LAB01,DC=ACME");

		// teaching the attributes
		when(attributes.size()).thenReturn(1);
		when(attributes.get(anyString())).thenReturn(attribute);

		// teaching the searchResult
		when(searchResult.getAttributes()).thenReturn(attributes);

		// teaching the searchResultEnumeration return a valid searchResult (the user
		// exist in AD)
		when(searchResultEnumeration.hasMoreElements()).thenReturn(true, true, false)
				// false to a invalid searchResult (the user don't exist in AD)
				.thenReturn(false);
		when(searchResultEnumeration.next()).thenReturn(searchResult);

		// teaching the dirContext
		when(dirContext.search(nullable(String.class), eq("(&(objectclass=user)(sAMAccountName=john))"),
				any(SearchControls.class))).thenReturn(searchResultEnumeration);
		when(dirContext.search(nullable(String.class), eq("(&(objectclass=user)(sAMAccountName=xxjohn))"),
				any(SearchControls.class))).thenReturn(searchResultEnumeration);

		/* ASSERTS */

		// ------- assert an existing user in the AD groups...
		List<String> johnGroups = asList("firewall-advance", "firewall-vpn", "firewall-inf", "inf", "Color Print",
				"Administrators", "Domain Admins");
		assertEquals(ad.searchUserGroups("john"), johnGroups);

		// ------- assert an don't existing user in the AD groups...
		List<String> xxjohnGroups = asList();
		assertEquals(ad.searchUserGroups("xxjohn"), xxjohnGroups);

	}

	@Test
	void testSearchUsersGroup() throws NamingException, ActiveDirectoryException {

		ReflectionTestUtils.setField(ad, "isHandShakeMade", true);

		// teaching the attribute
		when(attribute.size()).thenReturn(5);
		when(attribute.get(0)).thenReturn("CN=user2.brain,OU=SUP-TS,OU=roomTS,DC=LAB01,DC=ACME");
		when(attribute.get(1)).thenReturn("CN=alan.turing,OU=SUP,OU=room,DC=LAB01,DC=ACME");
		when(attribute.get(2)).thenReturn("CN=linus.torvalds,OU=SUP-TS,OU=roomTS,DC=LAB01,DC=ACME");
		when(attribute.get(3)).thenReturn("CN=jonh.gates,OU=SUP-TS,OU=roomTS,DC=LAB01,DC=ACME");
		when(attribute.get(4)).thenReturn("CN=sales02,OU=SUP,OU=room,DC=LAB01,DC=ACME");

		// teaching the attributes
		when(attributes.size()).thenReturn(1);
		when(attributes.get(anyString())).thenReturn(attribute);

		// teaching the searchResult
		when(searchResult.getAttributes()).thenReturn(attributes);

		// teaching the searchResultEnumeration return a valid searchResult (the group
		// exist in AD)
		when(searchResultEnumeration.hasMoreElements()).thenReturn(true, true, false)
				// false to a invalid searchResult (the group don't exist in AD)
				.thenReturn(false);
		when(searchResultEnumeration.next()).thenReturn(searchResult);

		// teaching the dirContext
		when(dirContext.search(nullable(String.class), eq("CN=sup"), any(SearchControls.class)))
				.thenReturn(searchResultEnumeration);
		when(dirContext.search(nullable(String.class), eq("CN=zzz"), any(SearchControls.class)))
				.thenReturn(searchResultEnumeration);

		/* ASSERTS */

		// ------- assert an existing group in the AD groups...
		List<String> usersSupGroup = asList("user2.brain", "alan.turing", "linus.torvalds", "jonh.gates", "sales02");
		assertEquals(ad.searchUsersGroup("sup"), usersSupGroup);

		// ------- assert an don't existing group in the AD groups...
		List<String> groupDontExist = asList();
		assertEquals(ad.searchUsersGroup("zzz"), groupDontExist);
	}

	@Test
	void testSearchUserObjects() throws NamingException, ActiveDirectoryException {

		ReflectionTestUtils.setField(ad, "isHandShakeMade", true);

		// teaching the attribute
		when(attribute.get(0)).thenReturn("david")
				.thenReturn("mary")
				.thenReturn("elizabeth")
				.thenReturn("glen");

		// teaching the attributes
		when(attributes.get(eq("cn"))).thenReturn(attribute);

		// teaching the searchResult
		when(searchResult.getAttributes()).thenReturn(attributes);

		// teaching the searchResultEnumeration
		when(searchResultEnumeration.next()).thenReturn(searchResult);
		when(searchResultEnumeration.hasMore()).thenReturn(true)
				.thenReturn(true)
				.thenReturn(true)
				.thenReturn(true)
				.thenReturn(false);

		// teaching the dirContext
		when(dirContext.search(
				nullable(String.class),
				eq("(&(objectCategory=person)(objectClass=user)(sAMAccountName=*da*))"),
				any(SearchControls.class)))
				.thenReturn(searchResultEnumeration);
		when(dirContext.search(
				nullable(String.class),
				eq("(&(objectCategory=person)(objectClass=user)(sAMAccountName=*zzzz*))"),
				any(SearchControls.class)))
				.thenReturn(new NamingEnumeration<SearchResult>() {
					@Override
					public SearchResult nextElement() {
						return null;
					}

					@Override
					public boolean hasMoreElements() {
						return false;
					} // simply return false...

					@Override
					public SearchResult next() throws NamingException {
						return null;
					}

					@Override
					public boolean hasMore() throws NamingException {
						return false;
					}

					@Override
					public void close() throws NamingException {
					}
				});

		/* ASSERTS */

		// valid search... users exist in AD.
		List<String> users = asList("david", "mary", "elizabeth", "glen");
		assertEquals(users, ad.searchUserObjects("da"));

		// valid search... but no username matches zzzz
		List<String> usersDontMatch = asList();
		assertEquals(usersDontMatch, ad.searchUserObjects("zzzz"));
	}

	@Test
	void testSearchGroupObjects() throws NamingException, ActiveDirectoryException {

		ReflectionTestUtils.setField(ad, "isHandShakeMade", true);

		// teaching the attribute
		when(attribute.get(0)).thenReturn("inf")
				.thenReturn("firewall-inf");

		// teaching the attributes
		when(attributes.get(eq("cn"))).thenReturn(attribute);

		// teaching the searchResult
		when(searchResult.getAttributes()).thenReturn(attributes);

		// teaching the searchResultEnumeration
		when(searchResultEnumeration.next()).thenReturn(searchResult);
		when(searchResultEnumeration.hasMore()).thenReturn(true)
				.thenReturn(true)
				.thenReturn(false);

		// teaching the dirContext
		when(dirContext.search(
				nullable(String.class),
				eq("(&(objectCategory=group)(sAMAccountName=*inf*))"),
				any(SearchControls.class)))
				.thenReturn(searchResultEnumeration);
		when(dirContext.search(
				nullable(String.class),
				eq("(&(objectCategory=group)(sAMAccountName=*zzzz*))"),
				any(SearchControls.class)))
				.thenReturn(new NamingEnumeration<SearchResult>() {
					@Override
					public SearchResult nextElement() {
						return null;
					}

					@Override
					public boolean hasMoreElements() {
						return false;
					} // simply return false...

					@Override
					public SearchResult next() throws NamingException {
						return null;
					}

					@Override
					public boolean hasMore() throws NamingException {
						return false;
					}

					@Override
					public void close() throws NamingException {
					}
				});

		/* ASSERTS */

		// valid search... users exist in AD.
		List<String> groups = asList("inf", "firewall-inf");
		assertEquals(groups, ad.searchGroupObjects("inf"));

		// valid search... but no username matches zzzz
		List<String> groupsDontMatch = asList();
		assertEquals(groupsDontMatch, ad.searchGroupObjects("zzzz"));
	}
}
