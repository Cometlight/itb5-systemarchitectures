package itb5;

import java.util.Hashtable;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

public class FHV_LDAP {
	public static void main(String[] args) {
		Hashtable<String, String> env = new Hashtable<>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, "ldaps://ldap.fhv.at:636/dc=uclv,dc=net");

		try {
			DirContext dirContext = new InitialDirContext(env);

			SearchControls searchControls = new SearchControls();
			String filter = "(&(uid=fha3027)(cn=*))";
			NamingEnumeration<SearchResult> res = dirContext.search("ou=fhv,ou=People", filter, searchControls);
			System.out.println("");
			// Search.printSearchEnumeration(res);
			while (res.hasMore()) {
				SearchResult r = res.next();
				System.out.println(r);
			}

			System.out.println("# # # #");
//			DirContext schema = dirContext.getSchema("ou=People");
//			NamingEnumeration<Binding> bindings = schema.listBindings("ClassDefinition");
//			while (bindings.hasMore()) {
//				Binding bd = bindings.next();
//				System.out.println(bd.getName() + ": " + bd.getObject() + "  -  " + bd.getClassName());
//				bd.getObject()
//			}
			browseRecursive(dirContext, 2);
			
//			System.out.println(dirContext.getAttributes(""));
			

		} catch (NamingException e) {
			e.printStackTrace();
			return;
		}

	}

	public static final void browseRecursive(Context ctx, int depth) throws NamingException {
		NamingEnumeration<Binding> namingEnum = ctx.listBindings("");
		while (namingEnum.hasMore()) {
			Binding bnd = namingEnum.next();
			if (bnd.getObject() instanceof Context) {
				Context curCtx = (Context) bnd.getObject();
				for (int i = 0; i < depth * 2; ++i)
					System.out.print(' ');
				System.out.println(bnd.getName());
				browseRecursive(curCtx, depth + 1);
			}
		}
	}
}
