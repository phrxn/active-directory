package com.quazzom.active_directory;

import java.util.HashMap;
import java.util.Map;

public class ActiveDirectoryUtils {

	private static final Map<Integer, String> mapErrorCodes = new HashMap<Integer, String>();
	private static final Map<String, String> mapErro49Details = new HashMap<String, String>();

	static {
		mapErrorCodes.put(0, "LDAP_SUCCESS");
		mapErrorCodes.put(1, "LDAP_OPERATIONS_ERROR");
		mapErrorCodes.put(2, "LDAP_PROTOCOL_ERROR");
		mapErrorCodes.put(3, "LDAP_TIMELIMIT_EXCEEDED");
		mapErrorCodes.put(4, "LDAP_SIZELIMIT_EXCEEDED");
		mapErrorCodes.put(5, "LDAP_COMPARE_FALSE");
		mapErrorCodes.put(6, "LDAP_COMPARE_TRUE");
		mapErrorCodes.put(7, "LDAP_AUTH_METHOD_NOT_SUPPORTED");
		mapErrorCodes.put(8, "LDAP_STRONG_AUTH_REQUIRED");
		mapErrorCodes.put(9, "reserved.");
		mapErrorCodes.put(10, "LDAP_REFERRAL");
		mapErrorCodes.put(11, "LDAP_ADMINLIMIT_EXCEEDED");
		mapErrorCodes.put(12, "LDAP_UNAVAILABLE_CRITICAL_EXTENSION");
		mapErrorCodes.put(13, "LDAP_CONFIDENTIALITY_REQUIRED");
		mapErrorCodes.put(14, "LDAP_SASL_BIND_IN_PROGRESS");
		mapErrorCodes.put(15, "Not used.");
		mapErrorCodes.put(16, "LDAP_NO_SUCH_ATTRIBUTE");
		mapErrorCodes.put(17, "LDAP_UNDEFINED_TYPE");
		mapErrorCodes.put(18, "LDAP_INAPPROPRIATE_MATCHING");
		mapErrorCodes.put(19, "LDAP_CONSTRAINT_VIOLATION");
		mapErrorCodes.put(20, "LDAP_TYPE_OR_VALUE_EXISTS");
		mapErrorCodes.put(21, "LDAP_INVALID_SYNTAX");
		mapErrorCodes.put(22, "Not used.");
		mapErrorCodes.put(23, "Not used.");
		mapErrorCodes.put(24, "Not used.");
		mapErrorCodes.put(25, "Not used.");
		mapErrorCodes.put(26, "Not used.");
		mapErrorCodes.put(27, "Not used.");
		mapErrorCodes.put(28, "Not used.");
		mapErrorCodes.put(29, "Not used.");
		mapErrorCodes.put(30, "Not used.");
		mapErrorCodes.put(31, "Not used.");
		mapErrorCodes.put(32, "LDAP_NO_SUCH_OBJECT");
		mapErrorCodes.put(33, "LDAP_ALIAS_PROBLEM");
		mapErrorCodes.put(34, "LDAP_INVALID_DN_SYNTAX");
		mapErrorCodes.put(35, "LDAP_IS_LEAF(Some Server RESERVED)");
		mapErrorCodes.put(36, "LDAP_ALIAS_DEREF_PROBLEM");
		mapErrorCodes.put(37, "reserved.");
		mapErrorCodes.put(38, "reserved.");
		mapErrorCodes.put(39, "reserved.");
		mapErrorCodes.put(40, "reserved.");
		mapErrorCodes.put(41, "reserved.");
		mapErrorCodes.put(42, "reserved.");
		mapErrorCodes.put(43, "reserved.");
		mapErrorCodes.put(44, "reserved.");
		mapErrorCodes.put(45, "reserved.");
		mapErrorCodes.put(46, "reserved.");
		mapErrorCodes.put(47, "reserved.");
		mapErrorCodes.put(48, "LDAP_INAPPROPRIATE_AUTH");
		mapErrorCodes.put(49, "LDAP_INVALID_CREDENTIALS");
		mapErrorCodes.put(50, "LDAP_INSUFFICIENT_ACCESS");
		mapErrorCodes.put(51, "LDAP_BUSY");
		mapErrorCodes.put(52, "LDAP_UNAVAILABLE");
		mapErrorCodes.put(53, "LDAP_UNWILLING_TO_PERFORM");
		mapErrorCodes.put(54, "LDAP_LOOP_DETECT");
		mapErrorCodes.put(55, "reserved.");
		mapErrorCodes.put(56, "reserved.");
		mapErrorCodes.put(57, "reserved.");
		mapErrorCodes.put(58, "reserved.");
		mapErrorCodes.put(59, "reserved.");
		mapErrorCodes.put(60, "reserved.");
		mapErrorCodes.put(61, "reserved.");
		mapErrorCodes.put(62, "reserved.");
		mapErrorCodes.put(63, "reserved.");
		mapErrorCodes.put(64, "LDAP_NAMING_VIOLATION");
		mapErrorCodes.put(65, "LDAP_OBJECT_CLASS_VIOLATION");
		mapErrorCodes.put(66, "LDAP_NOT_ALLOWED_ON_NONLEAF");
		mapErrorCodes.put(67, "LDAP_NOT_ALLOWED_ON_RDN");
		mapErrorCodes.put(68, "LDAP_ALREADY_EXISTS");
		mapErrorCodes.put(69, "LDAP_NO_OBJECT_CLASS_MODS");
		mapErrorCodes.put(70, "LDAP_RESULTS_TOO_LARGE");
		mapErrorCodes.put(71, "LDAP_AFFECTS_MULTIPLE_DSAS");
		mapErrorCodes.put(72, "reserved.");
		mapErrorCodes.put(73, "reserved.");
		mapErrorCodes.put(74, "reserved.");
		mapErrorCodes.put(75, "reserved.");
		mapErrorCodes.put(76, "reserved.");
		mapErrorCodes.put(77, "reserved.");
		mapErrorCodes.put(78, "reserved.");
		mapErrorCodes.put(79, "reserved.");
		mapErrorCodes.put(80, "LDAP_OTHER");
		mapErrorCodes.put(81, "LDAP_SERVER_DOWN");
		mapErrorCodes.put(82, "LDAP_LOCAL_ERROR");
		mapErrorCodes.put(83, "LDAP_ENCODING_ERROR");
		mapErrorCodes.put(84, "LDAP_DECODING_ERROR");
		mapErrorCodes.put(85, "LDAP_TIMEOUT");
		mapErrorCodes.put(86, "LDAP_AUTH_UNKNOWN");
		mapErrorCodes.put(87, "LDAP_FILTER_ERROR");
		mapErrorCodes.put(88, "LDAP_USER_CANCELLED");
		mapErrorCodes.put(89, "LDAP_PARAM_ERROR");
		mapErrorCodes.put(90, "LDAP_NO_MEMORY");
		mapErrorCodes.put(91, "LDAP_CONNECT_ERROR");
		mapErrorCodes.put(92, "LDAP_NOT_SUPPORTED");
		mapErrorCodes.put(93, "LDAP_CONTROL_NOT_FOUND");
		mapErrorCodes.put(94, "LDAP_NO_RESULTS_RETURNED");
		mapErrorCodes.put(95, "LDAP_MORE_RESULTS_TO_RETURN");
		mapErrorCodes.put(96, "LDAP_CLIENT_LOOP");
		mapErrorCodes.put(97, "LDAP_REFERRAL_LIMIT_EXCEEDED");
		mapErrorCodes.put(100, "INVALID_RESPONSE");
		mapErrorCodes.put(101, "AMBIGUOUS_RESPONSE");
		mapErrorCodes.put(112, "TLS_NOT_SUPPORTED");
		mapErrorCodes.put(113, "LCUPRESOURCESEXHAUSTED");
		mapErrorCodes.put(114, "LCUPSECURITYVIOLATION");
		mapErrorCodes.put(115, "LCUPINVALIDDATA");
		mapErrorCodes.put(116, "LCUPUNSUPPORTEDSCHEME");
		mapErrorCodes.put(117, "LCUPRELOADREQUIRED");
		mapErrorCodes.put(118, "CANCELED");
		mapErrorCodes.put(119, "NOSUCHOPERATION");
		mapErrorCodes.put(120, "TOOLATE");
		mapErrorCodes.put(121, "CANNOTCANCEL");
		mapErrorCodes.put(122, "ASSERTIONFAILED");
		mapErrorCodes.put(123, "AUTHORIZATIONDENIED");

		// PT-BR messages...
		mapErro49Details.put("525", "O usuário não foi encontrado");
		mapErro49Details.put("52e", "Usuário e/ou senha inválido(s).");
		mapErro49Details.put("52f", "Sua conta tem restrições e não é possível continuar.");
		mapErro49Details.put("530", "Não é possível fazer login nesse momento (restrição de hora).");
		mapErro49Details.put("531", "Não é possível fazer login desse computador.");
		mapErro49Details.put("532", "A sua senha expirou.");
		mapErro49Details.put("533", "A sua conta está desabilitada.");
		mapErro49Details.put("701", "A sua conta expirou.");
		mapErro49Details.put("773", "Você deve trocar a senha do seu computador.");
		mapErro49Details.put("775", "A sua conta está bloqueada.");
	}

	private ActiveDirectoryUtils() {
	}

	/**
	 * Get a LDAP error message with many details and return a friendly message
	 * error.
	 *
	 * @param errorMessage the complex LDAP message error.
	 *
	 * @return a String with a friendly LDAP message.
	 *
	 * @throws ActiveDirectoryException if <b>errorMessage</b> isn't a valid LDAP
	 *                                  message.
	 *
	 */
	public static String getActiveDirectoryErrorMessage(String errorMessage) throws ActiveDirectoryException {

		// check if errorMessage is a LDAP error
		if (errorMessage == null || !errorMessage.contains("LDAP: error code"))
			throw new ActiveDirectoryException(
					String.format("The error message isn't a LDAP error message: %s.", errorMessage));

		String[] splitErrorMessage = errorMessage.split(",");

		// check if the splitErrorMessage has 4 strings
		if (splitErrorMessage.length != 4)
			throw new ActiveDirectoryException(
					String.format("The error message isn't a LDAP error message: %s.", errorMessage));

		// example: LDAP: error code 49 - 80090308: LdapErr: DSID-0C090439
		String ldapErrorPart = splitErrorMessage[0];

		// ["LDAP: error code 49 ", " 80090308: LdapErr: DSID-0C090439"]
		String[] ldapErroCodePart = ldapErrorPart.split("-");

		// "LDAP: error code 49 " -> becomes -> 49
		String errorCodeStr = ldapErroCodePart[0].replaceAll("\\D+", "");
		int errorCodeInt = Integer.valueOf(errorCodeStr);

		// example: 52e
		String erroCodeStrDetail = splitErrorMessage[2].replaceAll(" data ", "");

		if (errorCodeStr.equals(""))
			throw new ActiveDirectoryException("Could not get LDAP error code from error message.");

		String messageErro = "";

		if (errorCodeInt == 49)
			messageErro = mapErro49Details.get(erroCodeStrDetail);
		else
			messageErro = mapErrorCodes.get(errorCodeInt);

		return String.format("LDAP error %s: %s", errorCodeStr, messageErro);
	}

}
