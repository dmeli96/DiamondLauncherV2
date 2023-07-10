public class ProcessChecker implements Runnable {
		
	private final Process process;
	
	public ProcessChecker(Process process) {
		this.process = process;
	}

	@Override
	public void run() {
		while (true) {
			try {
				process.exitValue();
				break;
			} catch (Exception ignored) {

			}
			try {
				Thread.sleep(600L);
			} catch (Exception e) {
				break;
			}
		}
	}
}