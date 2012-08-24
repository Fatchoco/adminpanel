package struts.tkd.function;

import java.util.*;

public class removeListDuplicate {
	public static List remove(List a)
	{
		Set set = new LinkedHashSet();
		set.addAll(a);
		return new ArrayList(set);
	}
}