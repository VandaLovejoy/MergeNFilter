import java.util.*;import java.io.*;import java.math.*;
//
//  getMafAln.java
//  
//
//  Created by Martin Smith on 7/07/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

public class MergeNFilter {
	public static void main(String[] Args) throws IOException {
		int input = 0 ; 
		BufferedWriter SegDups=new BufferedWriter(new FileWriter( Args[0].substring(0,Args[0].lastIndexOf("_"))+"_segmtl_dups.txt") );
		BufferedWriter Out = new BufferedWriter(new FileWriter( Args[0].substring(0, Args[0].lastIndexOf("_")) +".maf"));;
		Out.write("##maf version=1 \n"+
				"# original dump date: Sun Jul 25 07:08:34 2010\n# ensembl release: 59\n"+
				"# emf comment: Alignments: 11 eutherian mammals EPO\n# emf comment: Region: Homo sapiens chromosome:GRCh37\n"); 
		
		while ( input != Args.length ) {
			String File = Args[ input ] ;
			BufferedReader Entry = new BufferedReader(new FileReader( File ));
			String Line ;
			while  ( (Line = Entry.readLine() ) != null  ) {
				if ( Line.length() != 0  && Line.charAt(0) == 'a' ) {
					Out.write( Line); 
				}
				else if ( Line.length() != 0  && Line.charAt(0) == 's'){
					String [][] Spps = new String [50][6];
					String[] SpHead = new String[50] ; 					
					int i = 0 ; 
					while ( Line.length() != 0 && Line.charAt(0) == 's'  ) {
						Spps[i] = Line.split("\\s+"); 
						for (int j = 0 ; Spps[i][1].toString().charAt(j) != '.' ; j++ ) 
							SpHead[i] = SpHead[i] + Spps[i][1].toString().charAt(j) ;
						i++ ; 
						Line = Entry.readLine() ; 
						if (Line == null) 
							System.exit(0) ; 
					}
					terminate: for (int x = 0 ; x != SpHead.length && SpHead[x] != null ; x++ ) {
						boolean printIt = true ;
						if (x == 0) {  
							SegDups.write("\na score=0\n"); 
							for (int col = 0 ; col != 6 ; col++ )
								SegDups.write( Spps[0][col]+"\t" ) ; 
							SegDups.write(Spps[0][6]+"\n"); 
						}
						
						for (int y = 0 ; y != x ; y++ ) {
							//System.out.println(SpHead[x]) ; 
							if ( SpHead[x].equals( SpHead[y] ) ) {
								printIt = false ; 
								for (int col = 0 ; col != 6 ; col++ )
									//include sequence or not???
									SegDups.write( Spps[x][col]+"\t" ) ; 
								SegDups.write(Spps[x][6]+"\n"); 
								continue terminate ;
							}
						}
						if ( printIt ) {
							for (int col = 0 ; col != 6 ; col++ )
								Out.write( Spps[x][col]+"\t" ) ; 
							Out.write( Spps[x][6]+"\n") ; 
						}
						
						
					}
				}
				Out.write("\n");

			}
			input++ ;	
			Entry.close() ; 
		}
		Out.close(); 
		SegDups.close(); 
		
	}
}
