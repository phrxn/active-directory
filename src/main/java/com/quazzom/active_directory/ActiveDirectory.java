package com.quazzom.active_directory;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

/**
 * Class representing an Active Directory, used to create connections to Active
 * Directory and perform simple searches.
 *
 * @author david
 *
 */
public class ActiveDirectory {

	private Properties properties;
	private DirContext dirContext;

	private String usersContainer;
	private String server; // ip or ad hostname, e.g.: server001 or 192.165.50.60
	private String port;
	private String username; // user to create the connection and do the searches
	private String password; // user password
	private String domain; // domain e.g.: mydomain.com

	private boolean isHandShakeMade;

	/**
	 * Default Constructor
	 *
	 * @param server   the hostname or ip for the Active Directory server.
	 * @param port     the port to connection.
	 * @param username the username for the connection and for the searches.
	 * @param password the password to username
	 * @param domain   the domani e.g.: mydomain.global
	 */
	public ActiveDirectory(String server, String port, String username, String password, String domain) {
		this.server = server;
		this.port = port;
		this.username = username;
		this.password = password;
		this.domain = domain;
		this.isHandShakeMade = false;
	}

	/**
	 * Create the connection with the Active Directory
	 *
	 * @throws ActiveDirectoryException if the username is a null or empty string
	 * @throws NamingException          if a naming exception is encountered
	 */
	public void handShake() throws NamingException, ActiveDirectoryException {

		if (username == null || username.equals(""))
			throw new ActiveDirectoryException("the username cannot be null or empty.");

		createUsersContainer(domain);

		properties = new Properties();
		properties.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		properties.put(Context.PROVIDER_URL, ("LDAP://" + server + ":" + port));
		properties.put(Context.SECURITY_AUTHENTICATION, "simple");
		properties.put(Context.SECURITY_PRINCIPAL, (username + "@" + domain));
		properties.put(Context.SECURITY_CREDENTIALS, password);

		dirContext = new InitialDirContext(properties);
		isHandShakeMade = true;
	}

	/**
	 * Returns a list containing the common name of the user's groups.
	 *
	 * @param username a string with the user name that will have its groups listed.
	 *
	 * @return a list with common names or an empty list if the user has not groups
	 *         or if the user is not found.
	 *
	 * @throws NamingException          if a naming exception is encountered
	 * @throws ActiveDirectoryException if the handShake method is not called before
	 *                                  this method
	 */
	public List<String> searchUserGroups(String username) throws NamingException, ActiveDirectoryException {
		return searchToObjectAttributes("(&(objectclass=user)(sAMAccountName=" + username + "))", "memberOf");
	}

	/**
	 * Returns a list containing the common name of users in a group.
	 *
	 * @param groupName a string with the group name that will have its users
	 *                  listed.
	 *
	 * @return a list with common names or an empty list if the group has not
	 *         members or if the group is not found.
	 *
	 * @throws NamingException          if a naming exception is encountered
	 * @throws ActiveDirectoryException if the handShake method is not called before
	 *                                  this method
	 */
	public List<String> searchUsersGroup(String groupName) throws NamingException, ActiveDirectoryException {
		return searchToObjectAttributes("CN=" + groupName, "member");
	}

	/**
	 * Search for common names of user objects where their common names contain
	 * parts of partOfUsername <br>
	 * and returns a list of those common names.
	 * <p>
	 * Example:
	 * <blockquote>
	 *
	 * <pre>
	 * For the user names in Active Directory: [arthur, paul, john, marie, oscar]
	 *
	 * If partOfUsername is: "ar"
	 *
	 * The list returned is: [arthur, marie, oscar]
	 * </pre>
	 *
	 * </blockquote>
	 *
	 * @param partOfUsername a string containing part of the objects common name
	 *
	 * @return a list with common names or an empty list if partOfUsername does not
	 *         match any common name of some user.
	 *
	 * @throws NamingException          if a naming exception is encountered
	 * @throws ActiveDirectoryException if the handShake method is not called before
	 *                                  this method
	 */
	public List<String> searchUserObjects(String partOfUsername) throws NamingException, ActiveDirectoryException {
		String searchFilter = "(&(objectCategory=person)(objectClass=user)(sAMAccountName=*" + partOfUsername + "*))";

		return searchForObjects(searchFilter);
	}

	/**
	 * Search for common names of group objects where their common names contain
	 * parts of partOfTheCommonName <br>
	 * and returns a list of those common names.
	 * <p>
	 * Example:
	 * <blockquote>
	 *
	 * <pre>
	 * For the group names in Active Directory: [records, info, sales, financial, inventory, directory, check-in]
	 *
	 * If partOfGroupName is: "in"
	 *
	 * The list returned is: [info, financial, inventory, check-in]
	 * </pre>
	 *
	 * </blockquote>
	 *
	 * @param partOfGroupName a string containing part of the objects common name
	 *
	 * @return a list with common names or an empty list if partOfGroupName does not
	 *         match any common name of some group.
	 *
	 * @throws NamingException          if a naming exception is encountered
	 * @throws ActiveDirectoryException if the handShake method is not called before
	 *                                  this method
	 */
	public List<String> searchGroupObjects(String partOfGroupName) throws NamingException, ActiveDirectoryException {
		String searchFilter = "(&(objectCategory=group)(sAMAccountName=*" + partOfGroupName + "*))";
		return searchForObjects(searchFilter);
	}

	/**
	 * Search for objects using a filter and returns a list with the common name of
	 * those objects.
	 *
	 * @param filter a string with the filter for the search
	 *
	 * @return a list common names {@link String}.
	 *
	 * @throws NamingException          if a naming exception is encountered
	 * @throws ActiveDirectoryException if the handShake method is not called before
	 *                                  this method
	 */
	private List<String> searchForObjects(String filter) throws NamingException, ActiveDirectoryException {

		if (!isHandShakeMade)
			throwAhandShakeException();

		List<String> listActiveDirectoryObjects = new ArrayList<String>();

		SearchControls ctls = new SearchControls();
		ctls.setReturningAttributes(new String[] { "cn" });
		ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);

		NamingEnumeration<SearchResult> answer = dirContext.search(usersContainer, filter, ctls);

		while (answer.hasMore()) {
			SearchResult searchResult = answer.next();

			Attributes attrs = searchResult.getAttributes();
			Attribute att = attrs.get("cn");
			listActiveDirectoryObjects.add((String) att.get(0));
		}

		return listActiveDirectoryObjects;
	}

	/**
	 * Search by the object attributes. For example: search the members of a group.
	 *
	 * @param searchFilter a string with ldpa syntax to filter.
	 * @param attribute    the object attribute
	 *
	 * @return a list with common names
	 *
	 * @throws NamingException          if a naming exception is encountered
	 * @throws ActiveDirectoryException if the handShake method is not called before
	 *                                  this method
	 */
	private List<String> searchToObjectAttributes(String searchFilter, String attribute)
			throws NamingException, ActiveDirectoryException {

		if (!isHandShakeMade)
			throwAhandShakeException();

		SearchControls ctls = new SearchControls();
		ctls.setReturningAttributes(new String[] { attribute });
		ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);

		NamingEnumeration<SearchResult> objectAttributes = dirContext.search(usersContainer, searchFilter, ctls);

		if (!objectAttributes.hasMoreElements())
			return new ArrayList<String>();

		// if the search were successful return the SearchResult's lasts common names
		return getLastCommonNames(objectAttributes, attribute);
	}

	/**
	 * Returns a list of common names.<br>
	 * <br>
	 * 1) This method loops through a
	 * {@link NamingEnumeration}<{@link SearchResult}><br>
	 * <br>
	 * 2) The common names are obtained from each {@link SearchResult} in
	 * {@link NamingEnumeration}<{@link SearchResult}><br>
	 * &nbsp;&nbsp;&nbsp;&nbspThese common names are the last common names of an
	 * attribute list of a {@link SearchResult} attribute.<br>
	 * &nbsp;&nbsp;&nbsp;&nbspThe {@link SearchResult} attribute is obtained through
	 * ID, this ID is <b>attributeIdToFilter</b><br>
	 * <p>
	 * Examples:
	 * <blockquote>
	 *
	 * <pre>
	 * if <b>searchResultEnumeration</b> param is equal the:
	 *          [[SearchResult: CN=inf,OU=INF,OU=acme: null:null:{member=member: CN=<u>user1</u>,CN=Users,DC=mydomain,DC=LOCAL, CN=<u>user2</u>,CN=Users,DC=mydomain,DC=LOCAL}],
	 *           [SearchResult: CN=inf,OU=VEN,OU=acme: null:null:{member=member: CN=<u>user3</u>,CN=Users,DC=mydomain,DC=LOCAL, CN=<u>user4</u>,CN=Users,DC=mydomain,DC=LOCAL}]
	 * and <b>attributeIdToFilter</b> is equal:
	 *           member
	 *
	 * The list of common name strings returned will be:
	 * 			[user1, user2, user3, user4]
	 * </pre>
	 *
	 * </blockquote>
	 *
	 * @param searchResultEnumeration a enumeration with SearchResult
	 * @param attributeIdToFilter     id to {@link SearchResult} attribute
	 *
	 * @return a list common names {@link String}
	 *
	 * @throws NamingException if a naming exception is encountered
	 */
	private List<String> getLastCommonNames(NamingEnumeration<SearchResult> searchResultEnumeration,
			String attributeIdToFilter) throws NamingException {
		List<String> commonNameListReturn = new ArrayList<String>();

		String attribute = "",
				commonName = "";

		while (searchResultEnumeration.hasMoreElements()) {

			SearchResult searchResult = (SearchResult) searchResultEnumeration.next();

			Attributes searchResultAtttributes = searchResult.getAttributes();

			// if searchResult has no attributes, continue
			if (searchResultAtttributes.size() == 0)
				continue;

			// get a list of attributes for (attributeIdToFilter) attribute in the
			// searchResult.
			Attribute attributesToGetLastCommonNames = searchResultAtttributes.get(attributeIdToFilter);

			// if list of attributes to attributeIdToFilter was not found in searchResult,
			// continue
			if (attributesToGetLastCommonNames == null)
				continue;

			// add last common name to each attribute in attributesToGetLastCommonNames to
			// commonNameListReturn
			for (int count = 0; count < attributesToGetLastCommonNames.size(); count++) {
				attribute = (String) attributesToGetLastCommonNames.get(count);

				// for the string: "CN=paul,CN=Users,DC=mydomain,DC=LOCAL", the commonName is:
				// "paul"
				commonName = attribute.substring(3, attribute.indexOf(","));

				commonNameListReturn.add(commonName);
			}

		}
		return commonNameListReturn;
	}

	/**
	 * Create the {@link ActiveDirectory#usersContainer}.
	 * <p>
	 * Examples:
	 * <blockquote>
	 *
	 * <pre>
	 * if <b>domain</b> param is equal the: <u>mydomain.global</u>. The {@link ActiveDirectory#usersContainer} is:
	 *
	 *     <b>DC=mydomain,DC=global</b>
	 * </pre>
	 *
	 * </blockquote>
	 *
	 * @param domain the domain to create usersContainer, e.g.:
	 *               <b>mydomain.global</b>
	 */
	private void createUsersContainer(String domain) {
		StringBuilder usersContainerTmp = new StringBuilder();

		String[] domainHierarchy = domain.split("\\.");

		for (int count = 0; count < domainHierarchy.length; count++) {
			String tmpDomain = domainHierarchy[count];

			usersContainerTmp.append("DC=")
					.append(tmpDomain);

			// if it is not the last item in domainHierarchy add a comma to the
			// usersContainerTmp.
			if (count < (domainHierarchy.length - 1))
				usersContainerTmp.append(",");
		}

		usersContainer = usersContainerTmp.toString();
	}

	/**
	 * centralize the handShake exceptions
	 *
	 * @throws ActiveDirectoryException
	 */
	private void throwAhandShakeException() throws ActiveDirectoryException {
		throw new ActiveDirectoryException("do a handShake before starting a search.");
	}
}
