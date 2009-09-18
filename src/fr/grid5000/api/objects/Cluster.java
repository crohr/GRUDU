package fr.grid5000.api.objects;

import java.util.Collection;
import java.util.Map;

public class Cluster {
	public String uid;
	public String model;
	public Collection<Link> links;
	public Map<String, Node> nodes;
	Cluster() {}
}
