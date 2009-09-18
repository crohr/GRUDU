/**
 *
 */
package net.sf.jftp.net.rsync;

/**
 * @author david
 *
 */
public class RsyncModel {

    private boolean recursive = false;
    private boolean update = false;
    private boolean inplace = false;
    private boolean append = false;
    private boolean safeLinks = false;
    private boolean ignoreExisting = false;
    private boolean deleteDuring = false;
    private boolean deleteAfter = false;
    private boolean a = true;
    private boolean z = true;
    private boolean b = false;
    private String backupDir = null;
    private String suffix = null;
    private boolean delete = false;

    public static final String RECURSIVE_OPTION= "-r";
    public static final String UPDATE_STRING="-u";
    public static final String INPLACE_OPTION = "--inplace";
    public static final String APPEND_OPTION = "--append";
    public static final String SAFE_LINKS_OPTION = "--safe-links";
    public static final String IGNORE_EXISTING_OPTION = "--ignore-existing";
    public static final String DELETE_DURING_OPTION = "--delete-during";
    public static final String DELETE_AFTER_OPTION = "--delete-after";
    public static final String A_OPTION = "-a";
    public static final String Z_OPTION = "-z";
    public static final String B_OPTION = "-b";
    public static final String BACKUP_OPTION = "--backup-dir=";
    public static final String SUFFIX_OPTION = "--suffix=";
    public static final String DELETE_OPTION = "--delete";
    
    public static final String CHECKED = "checked";
    public static final String UNCHECKED = "unchecked";

    /**
     * @return a
     */
    public boolean isA() {
        return a;
    }
    /**
     * @param a a à définir
     */
    public void setA(boolean a) {
        this.a = a;
    }
    /**
     * @return append
     */
    public boolean isAppend() {
        return append;
    }
    /**
     * @param append append à définir
     */
    public void setAppend(boolean append) {
        this.append = append;
    }
    /**
     * @return b
     */
    public boolean isB() {
        return b;
    }
    /**
     * @param b b à définir
     */
    public void setB(boolean b) {
        this.b = b;
    }
    /**
     * @return backupDir
     */
    public String getBackupDir() {
        return backupDir;
    }
    /**
     * @param backupDir backupDir à définir
     */
    public void setBackupDir(String backupDir) {
        this.backupDir = backupDir;
    }
    /**
     * @return delete
     */
    public boolean isDelete() {
        return delete;
    }
    /**
     * @param delete delete à définir
     */
    public void setDelete(boolean delete) {
        this.delete = delete;
    }
    /**
     * @return deleteAfter
     */
    public boolean isDeleteAfter() {
        return deleteAfter;
    }
    /**
     * @param deleteAfter deleteAfter à définir
     */
    public void setDeleteAfter(boolean deleteAfter) {
        this.deleteAfter = deleteAfter;
    }
    /**
     * @return deleteDuring
     */
    public boolean isDeleteDuring() {
        return deleteDuring;
    }
    /**
     * @param deleteDuring deleteDuring à définir
     */
    public void setDeleteDuring(boolean deleteDuring) {
        this.deleteDuring = deleteDuring;
    }
    /**
     * @return ignoreExisting
     */
    public boolean isIgnoreExisting() {
        return ignoreExisting;
    }
    /**
     * @param ignoreExisting ignoreExisting à définir
     */
    public void setIgnoreExisting(boolean ignoreExisting) {
        this.ignoreExisting = ignoreExisting;
    }
    /**
     * @return inplace
     */
    public boolean isInplace() {
        return inplace;
    }
    /**
     * @param inplace inplace à définir
     */
    public void setInplace(boolean inplace) {
        this.inplace = inplace;
    }
    /**
     * @return recursive
     */
    public boolean isRecursive() {
        return recursive;
    }
    /**
     * @param recursive recursive à définir
     */
    public void setRecursive(boolean recursive) {
        this.recursive = recursive;
    }
    /**
     * @return safeLinks
     */
    public boolean isSafeLinks() {
        return safeLinks;
    }
    /**
     * @param safeLinks safeLinks à définir
     */
    public void setSafeLinks(boolean safeLinks) {
        this.safeLinks = safeLinks;
    }
    /**
     * @return suffix
     */
    public String getSuffix() {
        return suffix;
    }
    /**
     * @param suffix suffix à définir
     */
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
    /**
     * @return update
     */
    public boolean isUpdate() {
        return update;
    }
    /**
     * @param update update à définir
     */
    public void setUpdate(boolean update) {
        this.update = update;
    }
    /**
     * @return z
     */
    public boolean isZ() {
        return z;
    }
    /**
     * @param z z à définir
     */
    public void setZ(boolean z) {
        this.z = z;
    }

    public String getRsyncOptions(){
    	String options = "";
    	if(isA()) options+=A_OPTION + " ";
    	if(isZ()) options+=Z_OPTION + " ";
    	if(isRecursive()) options+=RECURSIVE_OPTION + " ";
    	if(isUpdate()) options+=UPDATE_STRING + " ";
    	if(isB()){
    		options +=B_OPTION + " ";
    		options +=BACKUP_OPTION+"="+getBackupDir()+" ";
    		options +=SUFFIX_OPTION+"="+getSuffix()+" ";
    	}
    	if(isInplace()) options+=INPLACE_OPTION +" ";
    	if(isAppend()) options +=APPEND_OPTION + " ";
    	if(isSafeLinks()) options +=SAFE_LINKS_OPTION + " ";
    	if(isIgnoreExisting()) options +=IGNORE_EXISTING_OPTION+ " ";
    	if(isDeleteDuring()) options +=DELETE_DURING_OPTION + " ";
    	if(isDeleteAfter()) options +=DELETE_AFTER_OPTION + " ";
    	if(isDelete()) options +=DELETE_OPTION + " ";
    	return options;
    }

}