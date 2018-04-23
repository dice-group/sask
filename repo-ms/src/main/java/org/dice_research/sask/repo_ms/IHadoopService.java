package org.dice_research.sask.repo_ms;

import java.io.InputStream;

import javax.servlet.ServletOutputStream;

import org.dice_research.sask.repo_ms.hdfs.HDFSFile;
/**
 * @author André Sonntag
 */
public interface IHadoopService {

	/**
	 * This method uploads a file to the Hadoop System.
	 * 
	 * @param location
	 *            folder on the Hadoop System
	 * @param path
	 *            path in the location folder
	 * @param originalFileName
	 *            name of the uploaded file
	 * @param fis
	 *            contains the file
	 * @return
	 */
	boolean createFile(Location location, String path, String originalFileName, InputStream fis);

	/**
	 * This method downloads a file from the Hadoop System. The file will copy in
	 * the ServletOutputStream of a HttpServletResponse.
	 * 
	 * @param location
	 *            folder on the Hadoop System
	 * @param path
	 *            path in the location folder
	 * @param ops
	 *            contains the file
	 */
	void readFile(Location location, String path, ServletOutputStream ops);

	/**
	 * This method lists the structure of a folder. This include all subfolders and
	 * files.
	 * 
	 * @param location
	 *            folder on the Hadoop System
	 * @return a HDFSFile object
	 */
	HDFSFile getHdfsStructure(Location location);

	/**
	 * This method creates a new directory on the Hadoop System.
	 * 
	 * @param location
	 *            folder on the Hadoop System
	 * @param path
	 *            path in the location folder
	 * @return
	 */
	boolean createDirectory(Location location, String path);

	/**
	 * This method rename or move a file or directory on the Hadoop System.
	 * 
	 * @param location
	 *            folder on the Hadoop System
	 * @param from
	 *            source path in the location folder
	 * @param from
	 *            target path in the location folder
	 * @return
	 */
	boolean rename(Location location, String from, String to);

	/**
	 * This method delete a file or directory on the Hadoop System.
	 * 
	 * @param location
	 *            folder on the Hadoop System
	 * @param path
	 *            path in the location folder
	 * @return
	 */
	boolean delete(Location location, String path);

}