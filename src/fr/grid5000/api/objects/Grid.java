package fr.grid5000.api.objects;

import java.util.Collection;
import java.util.Map;

public class Grid {
	  public String uid;
	  public Collection<Link> links;
	  public Map<String,Site> sites;
	  public Map<String,Environment> environments;
	  Grid() { }
}
