///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package cm.cirt.calldatarecordmanagement.abou.csv;
//
///**
// *
// * @author antic
// */
///**
// * 
// */
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
////import cm.cirt.xls.util.ConvertionUtil;
////import cm.cirt.xls.util.SheetAttributes;
//
///**
// * @author <a href="mailto:venceslas.ngounou@cirt.cm">Venceslas NGOUNOU</a>
// * On 2 août 2018 at 03:14:00
// */
//public class ConvertionUtilcsv2xlsx {
//
//	/**
//	 * 
//	 */
//	public ConvertionUtilcsv2xlsx() {
//
//	}
//
//	/**
//	 * @author <a href="mailto:venceslas.ngounou@cirt.cm">Venceslas NGOUNOU</a>
//	 * 2 août 2018 : 03:14:00
//	 * @param args
//	 * @throws Exception 
//	 */
//	public static void main(String[] args) throws Exception {
////		testCsvToExcel();
//		
//		String root = "C:\\Users\\antic\\Documents\\NetBeansProjects\\RequisitionMONC\\695148837";
//		csvToExcelInATree(root);
//	}
//	
//	private static void csvToExcelInATree(String root) {
//		File rootFile = new File(root);
//		
//		boolean walkedthrough = false;
//		
//		if(!walkedthrough && rootFile.isDirectory()) {
//			System.out.println("Beginning Dir: " + rootFile.getAbsolutePath());
//			
//			List<SheetAttributes> sheetAttributes = new ArrayList<>();
//			File[] sons = rootFile.listFiles();
//			for(File son : sons) {
//				if(son.isDirectory()) {
//					csvToExcelInATree(son.getAbsolutePath());
//				} else {
//					if(son.getName().endsWith(".csv"))
//						sheetAttributes.add(new SheetAttributes(son.getAbsolutePath(), son.getName().replace("IdentificationDesAbonnees", "IA-").replace("Requisition_Identification_Numero", "RIdN-").replace("Requisition_Listing", "RL-").replace(".csv", "")));
//				}
//			}
//
//			try {
//				ConvertionUtil.csvToExcel(rootFile.getAbsolutePath() + "\\Requisition_" + rootFile.getName() + ".xlsx", sheetAttributes);
//				// Suppression du .csv après génération du .xlsx
//				for(SheetAttributes son : sheetAttributes)
//					new File(son.getFilename()).delete();
//			} catch (Exception e) {
//				System.out.println("Error walking through directory: " + root);
//				e.printStackTrace();
//			}
//			System.out.println("Ending Dir: " + rootFile.getAbsolutePath() + "\n");
//			walkedthrough = true;
//		}
//	}
//
//	/**
//	 * 
//	 * @author <a href="mailto:venceslas.ngounou@cirt.cm">Venceslas NGOUNOU</a>
//	 * 2 août 2018 : 04:59:00
//	 * @throws Exception
//	 */
////	private static void testCsvToExcel() throws Exception {
////
////		String path = "C:\\Users\\antic\\Documents\\NetBeansProjects\\RequisitionMONC\\695148837\\";
////
//////		String csvFilename = path + "IdentificationAbonnees624-01-404-1002.csv";
//////		String excelFilename = path + "IdentificationAbonnees624-01-404-1002.xls";
//////		ConvertionUtil.csvToEXCEL(csvFilename, excelFilename);
////		
////		SheetAttributes sa1 = new SheetAttributes(path + "IdentificationAbonnees624-01-404-1002.csv", "IA-624-01-404-1002");
////		SheetAttributes sa2 = new SheetAttributes(path + "Requisition_IdNumero624-01-404-1002.csv", "RIdN-624-01-404-1002");
////		SheetAttributes sa3 = new SheetAttributes(path + "Requisition_Listing624-01-404-1002.csv", "RL-624-01-404-1002");
////
////		ConvertionUtil.csvToExcel(path + "624-01-404-1002_123.xlsx", Arrays.asList(sa1, sa2, sa3));
////		
////	}
//
//}
//
