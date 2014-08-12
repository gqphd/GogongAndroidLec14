
public class GuguClass {
	public static void main(String[] args) {
		System.out.format("\n				< gugu >\n");
		
		for(int j = 1 ; j <= 9 ; j++){
			System.out.println();
			for(int i = 2 ; i <= 5 ; i++){//2~5
				System.out.format("%d X %d = %2d	", i, j, i*j);
			}
		}
		
		System.out.println();//line change
		
	}
}
