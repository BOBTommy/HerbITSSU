package Integrated;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.table.DefaultTableModel;

/**
 * 테이블의 내용을 csv파일로 내보내기
 * @param rootDir: Directory File instance that will be stored day, month, year dir and integrated file
 * @param data: Table data carrying whole data with date in first column
 */
public class ExcelExport {
	private DefaultTableModel data;
	private File file, dirFile;
	private String rootDIR, dayDIR, monthDIR, yearDIR;
	private FileWriter fw;
	private BufferedWriter bw;
	
	public ExcelExport(File rootDir, DefaultTableModel data){
		this.file = rootDir;
		this.rootDIR = rootDir.getPath();
		dayDIR =  rootDIR + "/DAY/";
		monthDIR = rootDIR + "/MONTH/";
		yearDIR = rootDIR+ "/YEAR/";
		this.data = data;
	}
	public void writeAll() throws IOException{
		String DATE_MAX_FORMAT = "yyyy-MM-dd HH:mm:ss.S";
		String DATE_FORMAT = "yyyy_MM_dd";
		SimpleDateFormat dmf = new SimpleDateFormat(DATE_MAX_FORMAT);
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		String before = "", temp = "";
		
		int i, j;
		int size = data.getRowCount();
		int column = data.getColumnCount();
		
		//통합 기록
		fw = new FileWriter(file + "/"+ sdf.format(new Date())+ ".csv");
		bw = new BufferedWriter(fw);
		for (i = 0 ; i < size; i ++) {
			for (j = 0; j < 7 ; j ++) {
				bw.write(data.getValueAt(i, j).toString());
				bw.write(",");
			}
			bw.write("\r\n");
		}
		bw.flush();
		fw.flush();
		bw.close();
		fw.close();
		
		int separate = 0;
		for (int DAY_MONTH_YEAR = 0; DAY_MONTH_YEAR < 3 ; DAY_MONTH_YEAR++) {
			before = "";
			switch (DAY_MONTH_YEAR) {
			case 0: 
				//일간 기록
				dirFile = new File(dayDIR);
				separate = 10;
				break;
			case 1:
				// 월간 기록
				dirFile = new File(monthDIR);
				separate = 7;
				break;
			case 2:
				// 연간 기록
				dirFile = new File(yearDIR);
				separate = 4; 
				break;
			}
			dirFile.mkdir();
			
			try {
				for (i = 0 ; i < size; i ++) {
					
					
					temp = sdf.format (dmf.parse(data.getValueAt(i, 0).toString())); //날짜 받아오기
					temp = temp.substring(0, separate);		
					if (before == "") {
						//초기화
						file = new File(dirFile.getPath() +"/"+ temp + ".csv");
						fw = new FileWriter(file);
						bw = new BufferedWriter(fw);
					}
					else if (before.compareTo(temp) != 0) {
						//다른 날짜가 오는 경우에 대해
						//다른 파일 열기
						bw.flush();
						fw.flush();
						bw.close();
						fw.close();
						
						file = new File(dirFile.getPath() +"/"+ temp + ".csv");
						fw = new FileWriter(file);
						bw = new BufferedWriter(fw);
					}
					for (j = 0; j < column ; j ++) {
						bw.write(data.getValueAt(i, j).toString());
						bw.write(",");
					}
					bw.write("\r\n");
					before = temp;
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			bw.flush();
			fw.flush();
			bw.close();
			fw.close();
		}
		
	}
}
