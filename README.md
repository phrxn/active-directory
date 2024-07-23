# Active Directory

## About

A simple Java wrapper to make simple Active Directory queries more user-friendly.

## Compile requirements
 - `maven` version 3.8.4 or newer
 - `jdk` version 1.8.0 update 331

## How compile
run `mvn compile` a folder called _output_ will be created with the jar inside it.

## Example
```
import com.quazzom.active_directory.ActiveDirectory;

public class Example
{

	public static void main(String[] args)
	{
		try
		{
			ActiveDirectory ad = new ActiveDirectory("ldapsrv", "389", "user", "password", "com.domain");
			ad.handShake();

			List<String> users = ad.searchUserGroups("sales");
			for(String user : users)
				System.out.println(user);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}
}
```