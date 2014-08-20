import java.util.Random;


public class ThTest {
	
	static class MyThread extends Thread{
		public String name;
		MyThread(String pName){
			this.name = pName;
		}

		@Override
		public void run() {
			while(!Thread.interrupted()){//until it ends..
				//do something
				System.out.println(name);

				//wait random time(0~200ms)
				long sec_ms_random = (long) (new Random().nextFloat() * 200);
				try {
					Thread.sleep(sec_ms_random);
				} catch (InterruptedException e) {
					e.printStackTrace();
					Thread.currentThread().interrupt();
				}
			}
		}
	}

	public static void main(String[] args) {
		
		//start multiple threads and run at the same time.
		MyThread[] threads = new MyThread[3];
		for(int i = 0 ; i < 3; i ++){
			threads[i] = new MyThread("T"+(i+1));
			threads[i].start();
		}
	}
}
