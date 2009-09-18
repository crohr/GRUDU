/****************************************************************************/
/* Utility class for g5k clusters manipulation                              */
/*                                                                          */
/* Author(s)                                                                */
/* - David Loureiro (David Loureiro@ens-lyon.fr)                            */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: G5kCluster.java,v 1.14 2008/01/09 15:04:45 dloureir Exp $
 * $Log: G5kCluster.java,v $
 * Revision 1.14  2008/01/09 15:04:45  dloureir
 * Changing the number of nodes for the sagittaire cluster of the Lyon site
 *
 * Revision 1.13  2007/10/24 12:51:05  dloureir
 * Removing some calls to the G5KCluster class by the corresponding calls to the G5KSite class
 *
 * Revision 1.12  2007/09/28 16:03:19  aamar
 * Updating GRUDU with OAR2 & new Grid5000 architecture
 *
 * Revision 1.11  2007/07/12 15:35:06  dloureir
 * Some typo corrections.
 *
 * Revision 1.10  2007/03/26 20:27:45  dloureir
 * Changing the capacity of the nancy cluster
 *
 * Revision 1.9  2007/03/16 13:32:21  dloureir
 * Adding paraquad cluster
 *
 * Revision 1.8  2007/03/12 09:39:17  dloureir
 * Mise à jour du nombre de noeuds sur le cluster de lille
 *
 * Revision 1.7  2007/03/07 15:00:13  dloureir
 * Correctings some bugs or the javadoc
 *
 * Revision 1.6  2007/03/07 14:13:16  dloureir
 * Adding javadoc to GRUDU
 *
 * Revision 1.5  2007/02/27 14:10:07  dloureir
 * Adding sol cluster of sophia and removing some print outs
 *
 * Revision 1.4  2007/02/23 13:12:17  aamar
 * Add the method getClusterByOar.
 *
 ****************************************************************************/
package diet.gridr.g5k.util;

/**
 * Utility class for g5k clusters manipulation
 * 
 * TODO some dynamic behaviour should be added to that object ...
 *
 * @author David Loureiro (David Loureiro@ens-lyon.fr)
 *
 */
public class G5kCluster {

    /**
     * Index of the capricorne cluster of the Lyon's site
     */
    public final static int clusterLyonCapricorne     = 0;
    /**
     * Index of the sagittaire cluster of the Lyon's site
     */
    public final static int clusterLyonSagittaire     = 1;
    /**
     * Index of the azur cluster of the Sophia's site
     */
    public final static int clusterSophiaAzur    	    = 2;
    /**
     * Index of the helios cluster of the Sophia's site
     */
    public final static int clusterSophiaHelios  	    = 3;
    /**
     * Index of the Sol cluster of the Sophia's site
     */
    public final static int clusterSophiaSol            = 4;
    /**
     * Index of the paraquad cluster of the Rennes's site
     */
    public final static int clusterRennesParaquad	    = 5;
    /**
     * Index of the paravent cluster of the Rennes's site
     */
    public final static int clusterRennesParavent	    = 6;
    /**
     * Index of the parasol cluster of the Rennes's site
     */
    public final static int clusterRennesParasol	    = 7;
    /**
     * Index of the paramount cluster of the Rennes's site
     */
    public final static int clusterRennesParamount       = 8;
    /**
     * Index of the gdx cluster of the Orsay's site
     */
    public final static int clusterOrsayGdx    	    = 9;
    /**
     * Index of the netgdx cluster of the Orsay's site
     */
    public final static int clusterOrsayNetgdx    	    = 10;
    /**
     * Index of the bordeplage cluster of the Bordeaux's site
     */
    public final static int clusterBordeauxBordeplage   = 11;
    /**
     * Index of the bordereau cluster of the Bordeaux's site
     */
    public final static int clusterBordeauxBordereau   = 12;
    /**
     * Index of the bordemer cluster of the Bordeaux's site
     */
    public final static int clusterBordeauxBordemer   = 13;
    /**
     * Index of the violette cluster of the Toulouse's site
     */
    public final static int clusterToulouseViolette       = 14;
    /**
     * Index of the chuque cluster of the Lille's site
     */
    public final static int clusterLilleChuque    	    = 15;
    /**
     * Index of the chti cluster of the Lille's site
     */
    public final static int clusterLilleChti    	    = 16;
    /**
     * Index of the chicon cluster of the Lille's site
     */
    public final static int clusterLilleChicon    	    = 17;
    /**
     * Index of the grelon cluster of the Nancy's site
     */
    public final static int clusterNancyGrelon    	    = 18;
    /**
     * Index of the grillon cluster of the Nancy's site
     */
    public final static int clusterNancyGrillon    	    = 19;
    /**
     * Index of the Idcalc cluster of the Grenoble's site
     */
    public final static int clusterGrenobleIdcalc 	    = 20;
    /**
     * Index of the ICluster2 cluster of the Grenoble's site
     */
    public final static int clusterGrenobleIdpot  = 21;

    /**
     * Array of oar Frontals
     */
    private final static String [] oarFrontals
        = {"frontend.lyon.grid5000.fr", "frontend.lyon.grid5000.fr",
           "frontend.sophia.grid5000.fr", "frontend.sophia.grid5000.fr","frontend.sophia.grid5000.fr",
           "frontend.rennes.grid5000.fr", "frontend.rennes.grid5000.fr","frontend.rennes.grid5000.fr", "frontend.rennes.grid5000.fr", 
           "frontend.orsay.grid5000.fr","fronted.orsay.grid5000.fr",
           "frontend.bordeaux.grid5000.fr","frontend.bordeaux.grid5000.fr","frontend.bordeaux.grid5000.fr",
           "frontend.toulouse.grid5000.fr",
           "frontend.lille.grid5000.fr","frontend.lille.grid5000.fr","frontend.lille.grid5000.fr",
           "frontend.nancy.grid5000.fr","frontend.nancy.grid5000.fr",
           "frontend.grenoble.grid5000.fr","frontend.grenoble.grid5000.fr"
    };

    /**
     * Array of cluster names
     */
    private final static String[] clusterNames
        = {"Lyon--Capricorne", "Lyon--Sagittaire",
           "Sophia--Azur", "Sophia--Helios","Sophia--Sol",
           "Rennes--Paraquad","Rennes--Paravent","Rennes--Parasol","Rennes--Paramount",
           "Orsay--Gdx","Orsay--Netgdx",
           "Bordeaux--Bordeplage","Bordeaux--Bordereau","Bordeaux--Bordemer",
           "Toulouse--Violette",
           "Lille--Chuque","Lille--Chti","Lille--Chicon",
           "Nancy--Grelon","Nancy--Grillon",
           "Grenoble--Idcalc","Grenoble--Idpot"};

    /**
     * Cluster name as defined in properties
     */
    private final static String[] clusterNameProperties
    = {"capricorne", "sagittaire",
        "azur", "helios","sol",
        "paraquad","paravent","parasol","paramount",
        "gdx","netgdx",
        "bordeplage","bordereau","bordemer",
        "violette",
        "chuque","chti","chicon",
        "grelon","grillon",
        "idcalc", "idpot"};
    
    /**
     * Corresponding site indexes for each cluster
     */
    private final static int[] clusterSites = {
    	0,0,
    	1,1,1,
    	2,2,2,2,
    	3,3,
    	4,4,4,
    	5,
    	6,6,6,
    	7,7,
    	8,8
    	};

    /**
     * Array containing the capacities of each cluster
     */
    private final static int[] clusterCapacities = {
    	56,79, // Lyon
    	72,56,50, // Sophia
    	64,99,64,33, // Rennes
    	312,30, // Orsay
    	51,93,48, // Bordeaux
    	57, // Toulouse
    	53,40,26, // Lille
    	120,47, // Nancy
    	12,12	// Grenoble
    	};

    /**
     * Name of the current cluster
     */
    String name = null;
    /**
     * OarFrontal of the cluster
     */
    String oarFrontal = null;
    /**
     * Capacity of the current cluster
     */
    int capacity = 0;

    /**
     * Default constructor taking in argument a name and an oar frontale
     *
     * @param name name of the cluster
     * @param oarFrontal oar frontal of the cluster
     */
    public G5kCluster(String name, String oarFrontal){
        this.name = name;
        this.oarFrontal = oarFrontal;
        capacity = getCapacityForCluster(name);
    }

    /**
     * Second constructor taking in argument the index of the corresponding cluster
     *
     * @param index index of the cluster
     */
    public G5kCluster(int index){
        this.name = getNameForIndex(index);
        this.oarFrontal = getOarFrontalForIndex(index);
        this.capacity = getCapacityForIndex(index);
    }

    /**
     * Method returning the capacity of the cluster
     *
     * @return the capacity capacity of the cluster
     */
    public int getCapacity() {
        return capacity;
    }
    /**
     * Method setting the capacity of the cluster
     *
     * @param capacity the capacity to set
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
    /**
     * Method returning the name of the cluster
     *
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * Method setting the name of the cluster
     *
     * @param clusterName the name to set
     */
    public void setName(String clusterName) {
        this.name = clusterName;
    }
    /**
     * Method returning the oar frontale of the cluster
     *
     * @return the oarFrontal
     */
    public String getOarFrontal() {
        return oarFrontal;
    }
    /**
     * Method setting the oar frontale of the cluster
     *
     * @param oarFrontal the oarFrontal to set
     */
    public void setOarFrontal(String oarFrontal) {
        this.oarFrontal = oarFrontal;
    }

    /**
     * Method returning the name of a cluster from its index
     *
     * @param index index of the cluster
     * @return name of the corresponding cluster
     */
    public static String getNameForIndex(int index){
        if(index < 0 || index > (clusterNames.length -1)) return null;
        return clusterNames[index];
    }

    /**
     * Method returning the index of a cluster from its name
     *
     * @param aName name of the cluster
     * @return index of the cluster
     */
    public static int getIndexForCluster(String aName){
        for (int ix=0; ix< clusterNames.length; ix++) {
            if (aName.equals(clusterNames[ix]))
                return ix;
        }
        return -1;
    }

    /**
     * Method returning the capacity of the cluster from its name
     *
     * @param aName name of the cluster
     * @return capacity of the cluster
     */
    public static int getCapacityForCluster(String aName){
        int index = getIndexForCluster(aName);
        if(index == -1) return 0;
        return clusterCapacities[index];
    }

    /**
     * Method returning the capacity of a cluster from its index
     *
     * @param index index of a cluster
     * @return capacity of the cluster
     */
    public static int getCapacityForIndex(int index){
        if(index < 0 || index > (clusterCapacities.length-1)) return 0;
        return clusterCapacities[index];
    }

    /**
     * Method returning the Oar frontal from the name of a cluster
     *
     * @param aName the name of a cluster
     * @return the oar frontal of the cluster
     */
    public static String getOarFrontalForCluster(String aName){
        int index = getIndexForCluster(aName);
        if(index == -1) return null;
        return oarFrontals[getIndexForCluster(aName)];
    }

    /**
     * Method returning the the oar frontal of a cluster from its index
     *
     * @param index index of a cluster
     * @return oar frontal of the cluster
     */
    public static String getOarFrontalForIndex(int index){
        if(index < 0  || index > (oarFrontals.length-1)) return null;
        return oarFrontals[index];
    }

    /**
     * Method returning the index of the site of this cluster
     *
     * @return the index of a site for this cluster
     */
    public int getSiteIndexForCluster(){
        return clusterSites[getIndexForCluster(name)];
    }

    /**
     * Method returning the index of the site of a cluster from an index
     *
     * @param index the index of a site
     * @return the index of a site for a cluster from an index
     */
    public static int getSiteIndexForCluster(int index){
        if(index < 0 || index > (clusterSites.length-1)) return -1;
        return clusterSites[index];
    }

    /**
     * Method returning the number of clusters of Grid5000
     *
     * @return the number of clusters
     */
    public static int getClustersNumber(){
        return clusterNames.length;
    }

    /**
     * Method returning the array containing the clusters capacities
     *
     * @return the clusterCapacities
     */
    public static int[] getClusterCapacities() {
        return clusterCapacities;
    }

    /**
     * Method returning the array containing the names of the clusters
     *
     * @return the clusterNames
     */
    public static String[] getClusterNames() {
        return clusterNames;
    }

    /**
     * Method returning the array containing the names of the clusters
     *
     * @return the oarFrontals
     */
    public static String[] getOarFrontals() {
        return oarFrontals;
    }

    /**
     * Method returning the index of a cluster by its oar frontale
     *
     * @param oarFrontal an oar frontal
     * @return the index of the cluster corresponding to that oar frontal
     */
    public static int getClusterByOar(String oarFrontal) {
        for (int ix=0; ix< oarFrontals.length; ix++) {
            if (oarFrontal.equals(oarFrontals[ix]))
                return ix;
        }
        return -1;
    }

    /**
     * @param clusterIdx cluster index
     * @return the cluster name as defined in properties
     */
    public static String getClusterNameProperties(int clusterIdx) {
        return clusterNameProperties[clusterIdx];
    }
}