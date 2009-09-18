package net.sf.jftp.util ;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import java.util.zip.*;

import javax.sound.sampled.TargetDataLine;

import net.sf.jftp.util.tar.InvalidHeaderException;
import net.sf.jftp.util.tar.TarArchive;
import net.sf.jftp.util.tar.TarBuffer;
import net.sf.jftp.util.tar.TarEntry;
import net.sf.jftp.util.tar.TarEntryEnumerator;
import net.sf.jftp.util.tar.TarInputStream;
import net.sf.jftp.util.tar.TarOutputStream;
import net.sf.jftp.util.tar.TarProgressDisplay;

/**
 * Classe d'utilitaires pour les compressions et d�compression Zip, GZip, JAR...
 * @author iubito (Sylvain Machefert)
 */
public class OutilsZip {

	/** Taille du buffer pour les lectures/�critures */
	private static final int BUFFER_SIZE = 8 * 1024;
	private static final int blockSize = TarBuffer.DEFAULT_BLKSIZE;
	/**
	 * D�compresse un GZIP contenant un fichier unique.
	 *
	 * Efface le filedest avant de commencer.
	 * @param gzsource Fichier GZIP � d�compresser
	 * @param filedest Nom du fichier destination o� sera sauvegard� le fichier
	 * contenu dans le GZIP.
	 * @throws FileNotFoundException si le fichier GZip n'existe pas
	 * @throws IOException
	 * @see http://javaalmanac.com/egs/java.util.zip/UncompressFile.html?l=rel
	 */
	public static void gunzip(String gzsource, String filedest)
		throws FileNotFoundException, IOException {
		// Open the compressed file
		GZIPInputStream in = new GZIPInputStream(new FileInputStream(gzsource));
		try {
			BufferedInputStream bis = new BufferedInputStream(in);
			try {
				// Open the output file
				OutputStream out = new FileOutputStream(filedest);
				try {
					BufferedOutputStream bos = new BufferedOutputStream(out);
					try {
						// Transfer bytes from the compressed file to the output file
						byte[] buf = new byte[BUFFER_SIZE];
						int len;
						while ((len = bis.read(buf, 0, BUFFER_SIZE)) != -1) {
							bos.write(buf, 0, len);
						}
						buf = null;
					}
					finally {
						bos.close();
					}
				}
				finally {
					out.close();
				}
			}
			finally {
				bis.close();
			}
		}
		finally {
			in.close();
		}
	}

	/**
	 * Compresse un fichier dans un GZIP.
	 *
	 * Efface le filedest avant de commencer.
	 * @param filesource Fichier � compresser
	 * @param gzdest Fichier GZIP cible
	 * @throws FileNotFoundException si le fichier source n'existe pas ou si le GZIP
	 * n'existe pas apr�s la compression
	 * @throws IOException
	 * @see http://javaalmanac.com/egs/java.util.zip/CompressFile.html?l=rel
	 * @see http://java.developpez.com/livres/penserenjava/?chap=12&page=3
	 */
	public static void gzip(String filesource, String gzdest)
		throws FileNotFoundException, IOException {
		// Create the GZIP output stream
		GZIPOutputStream out = new GZIPOutputStream(new FileOutputStream(gzdest));
		try {
			BufferedOutputStream bos = new BufferedOutputStream(out);
			try {
				// Open the input file
				FileInputStream in = new FileInputStream(filesource);
				try {
					BufferedInputStream bis = new BufferedInputStream(in);
					try {
						// Transfer bytes from the input file to the GZIP output stream
						byte[] buf = new byte[BUFFER_SIZE];
						int len;
						while ((len = bis.read(buf, 0, BUFFER_SIZE)) > 0) {
							bos.write(buf, 0, len);
						}
						buf = null;
					}
					finally {
						bis.close();
					}
				}
				finally {
					in.close();
				}
			}
			finally {
				bos.close();
			}
		}
		finally {
			out.close();
		}
		if (!new File(gzdest).exists())
			throw new FileNotFoundException("Le fichier " + gzdest + " n'a pas �t� cr��");
	}

	/**
	 * D�compresse l'archive Zip dans un r�pertoire.
	 * R�utilise les noms des r�pertoires lors de la d�compression.
	 * @param zipsrc
	 * @param basedirdest
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws SecurityException
	 */
	public static void unzipToDir(String zipsrc, String basedirdest)
		throws FileNotFoundException, IOException, SecurityException {

		unzipToDir(new FileInputStream(zipsrc), basedirdest);
	}

	/**
	 * D�compresse le flux <tt>InputStream</tt> Zip dans un r�pertoire.
	 * R�utilise les noms des r�pertoires lors de la d�compression.
	 * @param zipsrc
	 * @param basedirdest
	 * @throws SecurityException
	 * @throws IOException
	 */
	public static void unzipToDir(InputStream inzip, String basedirdest)
		throws IOException, SecurityException {

		File base = new File(basedirdest);
		if (!base.exists())
			base.mkdirs();

		try {
			CheckedInputStream checksum = new CheckedInputStream(inzip, new Adler32());
			try {
				//Buffer sur le zip
				BufferedInputStream bis = new BufferedInputStream(checksum);
				try {
					ZipInputStream zis = new ZipInputStream(bis);
					try {
						ZipEntry entry;
						File f;
						int count;
						byte[] buf = new byte[BUFFER_SIZE];
						BufferedOutputStream bos;
						FileOutputStream fos;
						//Parcours les entr�es du zip
						while ((entry = zis.getNextEntry()) != null) {
							f = new File(basedirdest, entry.getName());
							if (entry.isDirectory())
								f.mkdirs();
							else { //L'entry semble �tre un fichier
								//Si contient un / on cr�e les r�pertoires, car parfois on a pas dir/ avant dir/fichier.ext
								int l = entry.getName().lastIndexOf('/');
								if (l != -1) {
									new File(basedirdest, entry.getName().substring(0, l)).mkdirs();
								}
								fos = new FileOutputStream(f);
								try {
									bos = new BufferedOutputStream(fos, BUFFER_SIZE);
									try {
										//Ecriture du fichier
										while ((count = zis.read(buf, 0, BUFFER_SIZE)) != -1) {
											bos.write(buf, 0, count);
										}
									}
									finally {
										bos.close();
									}
								}
								finally {
									fos.close();
								}
							}
							if (entry.getTime() != -1) {
								f.setLastModified(entry.getTime());
							}
						}
					}
					finally {
						zis.close();
					}
				}
				finally {
					bis.close();
				}
			}
			finally {
				checksum.close();
			}
		} finally {
			inzip.close();
		}
	}

	/**
	 * Zippe r�cursivement un r�pertoire, en mettant les chemins de fichiers en relatif.
	 * <p>Les accents des noms de fichiers sont supprim�s, voir
	 * {@link OutilsString#sansAccents(String)} pour plus de d�tails.
	 * @param dirsource Le r�pertoire � compresser
	 * @param zipdest Le nom du fichier zip r�sultat
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void zipDir(String dirsource, String zipdest)
		throws FileNotFoundException, IOException {

		//Cr�ation d'un flux d'�criture vers un fichier
		FileOutputStream fos = new FileOutputStream(zipdest);
		try {
			//Ajout du checksum : Adler32 (plus rapide) ou CRC32
			CheckedOutputStream checksum = new CheckedOutputStream(fos, new Adler32());
			try {
				//Cr�ation d'un buffer de sortie afin d'am�liorer les performances
				BufferedOutputStream bos = new BufferedOutputStream(checksum, BUFFER_SIZE);
				try {
					//Cr�ation d'un flux d'�criture Zip vers le fichier � travers le buffer
					ZipOutputStream zos = new ZipOutputStream(bos);
					try {
						//Compression maximale
						try {
							zos.setMethod(ZipOutputStream.DEFLATED);
							zos.setLevel(Deflater.BEST_COMPRESSION);
						}
						catch (Exception ignor) {}
						zipDir(dirsource, null, zos);
					}
					finally {
						zos.close();
					}
				}
				finally {
					bos.close();
				}
			}
			finally {
				checksum.close();
			}
		}
		finally {
			fos.close();
		}
	}

	/**
	 * �tant donn� un r�pertoire base (qui n'est pas inclu dans les entr�es
	 * <tt>ZipEntry</tt>), le r�pertoire courant � zipper, et le <tt>ZipOutputStream</tt>
	 * de sortie, ajoute les fichiers dans le zip, ou s'appelle r�cursivement pour ajouter
	 * un r�pertoire fils.
	 * @param basedir
	 * @param currentdir
	 * @param zos
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @see http://www.developpez.net/forums/viewtopic.php?p=1724764
	 */
	private static void zipDir(String basedir, String currentdir, ZipOutputStream zos)
		throws FileNotFoundException, IOException {
		//create a new File object based on the directory we have to zip
		File zipDir = (currentdir != null) ? new File(basedir, currentdir) : new File(basedir);
		//get a listing of the directory content
		String[] dirList = zipDir.list();
		byte[] readBuffer = new byte[BUFFER_SIZE];
		int bytesIn = 0;
		//On met pas File.separator, mais "/" parce que c'est le caract�re utilis�
		//dans les ZIP.
		String currentdir2 = (currentdir != null) ? (currentdir + "/") : "";

		File f;
		FileInputStream fis;
		BufferedInputStream bis;
		ZipEntry anEntry;
		//Create an empty entry with just dir name, like WinZip, so unzipToDir will
		//behave correctly.
		if (currentdir2.length() > 0) {
			anEntry = new ZipEntry(currentdir2);
			zos.putNextEntry(anEntry);
			zos.closeEntry();
		}
		//loop through dirList, and zip the files
		for (int i = 0; i < dirList.length; i++) {
			f = new File(zipDir, dirList[i]);
			if (!f.exists())
				continue;
			if (f.isDirectory()) {
				//if the File object is a directory, call this
				//function again to add its content recursively
				zipDir(basedir, currentdir2 + dirList[i], zos);
				continue;
			}
			//if we reached here, the File object f was not a directory
			//create a FileInputStream on top of f
			fis = new FileInputStream(f);
			try {
				bis = new BufferedInputStream(fis, BUFFER_SIZE);
				try {
					//create a new zip entry
					anEntry = new ZipEntry(currentdir2 + dirList[i]);
					anEntry.setTime(f.lastModified());

					//place the zip entry in the ZipOutputStream object
					zos.putNextEntry(anEntry);
					//now write the content of the file to the ZipOutputStream
					while ((bytesIn = bis.read(readBuffer, 0, BUFFER_SIZE)) != -1) {
						zos.write(readBuffer, 0, bytesIn);
					}
					zos.closeEntry();
				}
				finally {
					bis.close();
				}
			}
			finally {
				//close the Stream
				fis.close();
			}
		}
	}
	
	private static ArrayList<String> listFilesOf(String aPath){
		ArrayList<String> output = new ArrayList<String>();
		File directory = new File(aPath);
		if(directory.isDirectory()){
			output.add(aPath+"/");
			for(File aFile : directory.listFiles()){
				String fileName = aFile.getAbsolutePath();
				if(aFile.isDirectory()) output.addAll(listFilesOf(fileName));
				else{
					output.add(fileName);
				}
			}
		}
		else{
			output.add(aPath);
		}
		return output;
	}
	
	public static void main(String args[]){
		tarGz("/home/david/.SunDownloadManager", "/home/david/tmp/test.tgz");
	}

	public static void tarGz(String dirToTarGz, String absolutePathOfOutputFile){
		
	    TarArchive archive = null;
	    OutputStream outStream =null;
	    try {
	        outStream = new FileOutputStream( absolutePathOfOutputFile );
	    }
	    catch ( IOException ex )
	    {
	        outStream = null;
	        ex.printStackTrace( System.err );
	    }
	    try {
	        outStream = new GZIPOutputStream( outStream );
	    }
	    catch ( IOException ex )
	    {
	        outStream = null;
	        ex.printStackTrace( System.err );
	    }
	    archive = new TarArchive( outStream, blockSize );
	    archive.setVerbose(true);
	    archive.setTarProgressDisplay(new OutilsZip.MyTarProgressionDiplay());
	    
	    ArrayList<String> listOfFiles = listFilesOf(dirToTarGz);
	    //String directoryName  =new File( dirToTarGz ).getName();
	    String baseDir = new File( dirToTarGz ).getParentFile().getAbsolutePath();
	    for(String files : listOfFiles){
	    	try {
	    		File f = new File( files );
	    		if(f.compareTo(new File(dirToTarGz)) != 0){
	    			String relativePath = f.getAbsolutePath().substring(baseDir.length()+1);
	    			TarEntry entry = new TarEntry(f);
	    			entry.setName(relativePath);
	    			entry.setUSTarFormat();
	    			archive.writeEntry( entry, false );
	    		}
	    	}
	    	catch ( IOException ex )
	    	{
	    		ex.printStackTrace( System.err );
	    	}
	    }

        if ( archive != null )                      // CLOSE ARCHIVE
        {
        try {
            archive.closeArchive();
            }
        catch ( IOException ex )
            {
            ex.printStackTrace( System.err );
            }
        }
	}

    public static void untarGz(String archiveToUnTarGz, String destinationDirectory){
        TarArchive archive = null;

        InputStream inStream = System.in;

        try {
            inStream = new FileInputStream( archiveToUnTarGz );
        }
        catch ( IOException ex )
        {
            inStream = null;
            ex.printStackTrace( System.err );
        }

        try {
            inStream = new GZIPInputStream( inStream );
        }
        catch ( IOException ex )
        {
            inStream = null;
            ex.printStackTrace( System.err );
        }

        archive = new TarArchive( inStream, blockSize );


        File destDir = new File( destinationDirectory );
        if ( ! destDir.exists() )
            {
            if ( ! destDir.mkdirs() )
                {
//                destDir = null;
                Throwable ex = new Throwable( "ERROR, mkdirs() on '" + destDir.getPath()+ "' returned false." );
                ex.printStackTrace( System.err );
                }
            }

        if ( destDir != null )
        {
            try {
                archive.extractContents( destDir );
            }
            catch ( InvalidHeaderException ex )
            {
                ex.printStackTrace( System.err );
            }
            catch ( IOException ex )
            {
                ex.printStackTrace( System.err );
            }
        }
        if ( archive != null )                      // CLOSE ARCHIVE
        {
        try {
            archive.closeArchive();
            }
        catch ( IOException ex )
            {
            ex.printStackTrace( System.err );
            }
        }
    }

    public static void getFilesFromTarGz(File Tgz,File outputDir) throws FileNotFoundException, IOException
    {

        Vector files = new Vector();

        FileInputStream fin = new FileInputStream(Tgz);
        TarInputStream tin = new TarInputStream( new GZIPInputStream(fin));
        TarEntryEnumerator enumerator = new TarEntryEnumerator(tin);



        while (enumerator.hasMoreElements())
        {
            TarEntry TE = (TarEntry)enumerator.nextElement();
            files.add(TE);
            File destPath = new File(outputDir.getAbsolutePath() + File.separatorChar + TE.getName());
            System.out.println("Processing " + destPath.getAbsoluteFile());
            if(!TE.isDirectory())
            {


                // start of new code
                String pathStr = destPath.getPath();
                int idx = pathStr.lastIndexOf(File.separatorChar);
                if (idx > 0)
                {
                    File destDir = new File(pathStr.substring(0,idx));
                    destDir.mkdirs();
                }
                // end of new code


                FileOutputStream fout = new FileOutputStream(destPath);
                tin.copyEntryContents(fout);
                fout.close();
            }
            else
            {
                destPath.mkdir();
            }
        }
        tin.close();

    }

    public static void getFilesFromTar(File Tar,File outputDir) throws FileNotFoundException, IOException
    {
        File ret[];

        Vector files = new Vector();

        FileInputStream fin = new FileInputStream(Tar);
        TarInputStream tin = new TarInputStream( fin);
        TarEntryEnumerator enumerator = new TarEntryEnumerator(tin);



        while (enumerator.hasMoreElements())
        {
            TarEntry TE = (TarEntry)enumerator.nextElement();
            files.add(TE);
            File destPath = new File(outputDir.getAbsolutePath() + File.separatorChar + TE.getName());
            System.out.println("Processing " + destPath.getAbsoluteFile());
            if(!TE.isDirectory())
            {


                // start of new code
                String pathStr = destPath.getPath();
                int idx = pathStr.lastIndexOf(File.separatorChar);
                if (idx > 0)
                {
                    File destDir = new File(pathStr.substring(0,idx));
                    destDir.mkdirs();
                }
                // end of new code


                FileOutputStream fout = new FileOutputStream(destPath);
                tin.copyEntryContents(fout);
                fout.close();
            }
            else
            {
                destPath.mkdir();
            }
        }
        tin.close();

    }
    
    public static class MyTarProgressionDiplay implements TarProgressDisplay{

		/* (non-Javadoc)
		 * @see net.sf.jftp.util.tar.TarProgressDisplay#showTarProgressMessage(java.lang.String)
		 */
		public void showTarProgressMessage(String msg) {
			// TODO Auto-generated method stub
			System.out.println("tar >> "+ msg);
		}
    	
    }
}
