package fr.grid5000.api.objects;

import java.util.Collection;
import java.util.Map;

public class Site {
	public String uid;
	public String name;
	public Collection<Link> links;
	public Map<String, Cluster> clusters;
	public Map<String, Environment> environments;
	public Float latitude;
	public Float longitude;
	Site() {}
}
