/**
 * 
 */
package ganglia.models.history;

/**
 * @author david
 *
 */
public interface HostXML {

	public static String HOST_ELEMENT = "host";
	public static String HOST_ATTRIBUTE_NAME = "name";
	public static String HOST_BYTES_IN = "bytes_in";
	public static String HOST_BYTES_OUT = "bytes_out";
	public static String HOST_PKTS_IN = "pkts_in";
	public static String HOST_PKTS_OUT = "pkts_out";
	public static String HOST_CPU_IDLE = "cpu_idle";
	public static String HOST_CPU_SYSTEM = "cpu_system";
	public static String HOST_CPU_USER = "cpu_user";
	public static String HOST_DISK_FREE = "disk_free";
	public static String HOST_DISK_TOTAL = "disk_total";
	public static String HOST_SWAP_FREE = "swap_free";
	public static String HOST_SWAP_TOTAL = "swap_total";
	public static String HOST_MEM_TOTAL = "mem_total";
	public static String HOST_MEM_FREE = "mem_free";
	public static String HOST_PROC_RUN = "proc_run";
	public static String HOST_PROC_TOTAL = "proc_total";
	public static String HOST_LOAD_ONE = "load_one";
	public static String HOST_LOAD_FIVE = "load_five";
	public static String HOST_LOAD_FIFTEEN = "load_fifteen";
}
