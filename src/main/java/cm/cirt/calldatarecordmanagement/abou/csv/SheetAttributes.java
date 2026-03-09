/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.cirt.calldatarecordmanagement.abou.csv;

/**
 *
 * @author antic
 */
/**
 * 
 */

/**
 * @author <a href="mailto:venceslas.ngounou@cirt.cm">Venceslas NGOUNOU</a>
 * On 2 août 2018 at 03:35:59
 */
public class SheetAttributes {

	private String filename;
	
	private String sheetname;
	
	/**
	 * 
	 */
	public SheetAttributes() {}	

	/**
	 * @param filename
	 * @param sheetname
	 */
	public SheetAttributes(String filename, String sheetname) {
		super();
		this.filename = filename;
		this.sheetname = sheetname;
	}


	/**
	 * Method to get the value of the field {@link filename}
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * Method to set the value of the field {@link filename}
	 * @param filename the filename to set
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

	/**
	 * Method to get the value of the field {@link sheetname}
	 * @return the sheetname
	 */
	public String getSheetname() {
		return sheetname;
	}

	/**
	 * Method to set the value of the field {@link sheetname}
	 * @param sheetname the sheetname to set
	 */
	public void setSheetname(String sheetname) {
		this.sheetname = sheetname;
	}
	
}
